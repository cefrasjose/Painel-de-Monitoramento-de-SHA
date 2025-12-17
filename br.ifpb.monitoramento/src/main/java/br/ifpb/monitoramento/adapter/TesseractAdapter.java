package br.ifpb.monitoramento.adapter;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TesseractAdapter implements ILeitorImagem {

    private final Tesseract tesseract;

    public TesseractAdapter() {
        this.tesseract = new Tesseract();
        //Caminho relativo para a pasta tessdata na raiz do projeto
        this.tesseract.setDatapath("tessdata");
        this.tesseract.setLanguage("eng");

        //Whitelist: Forca o Tesseract a ler APENAS numeros
        this.tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");
    }

    @Override
    public double extrairValor(File arquivo) throws LeituraException {
        try {
            //1 Carrega a imagem para a memoria (liberando o arquivo do disco)
            BufferedImage imagemOriginal = ImageIO.read(arquivo);
            if (imagemOriginal == null) {
                throw new LeituraException("Arquivo de imagem inválido ou corrompido.");
            }

            //2 Pre-processamento: Zoom (2x) e Escala de Cinza
            int novaLargura = imagemOriginal.getWidth() * 2;
            int novaAltura = imagemOriginal.getHeight() * 2;

            BufferedImage imagemTratada = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = imagemTratada.createGraphics();

            //Interpolacao de qualidade para manter os numeros nitidos
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(imagemOriginal, 0, 0, novaLargura, novaAltura, null);
            g.dispose();

            //3 Executa o OCR
            String texto = tesseract.doOCR(imagemTratada);

            //4 Logica de Filtro Ajustada para Hidrometro
            String[] possiveisNumeros = texto.split("\\s+");
            String melhorCandidato = "";

            for (String s : possiveisNumeros) {
                //Remove qualquer sujeira nao numerica
                String limpo = s.replaceAll("[^0-9]", "");

                //REGRA ATUALIZADA:
                //O hidrometro tem 5 digitos (ex: 00102). Aceitamos entre 3 e 8 para tolerancia.
                if (limpo.length() >= 3 && limpo.length() <= 6) {
                    // Pega o candidato mais longo dentro desse intervalo
                    if (limpo.length() > melhorCandidato.length()) {
                        melhorCandidato = limpo;
                    }
                }
            }

            System.out.println("   [OCR] Texto Bruto: [" + texto.replace("\n", " | ").trim() + "]");
            System.out.println("   [OCR] Melhor candidato (Inteiro): [" + melhorCandidato + "]");

            if (melhorCandidato.isEmpty()) {
                throw new LeituraException("Nenhum padrão de 5 dígitos encontrado na imagem.");
            }

            //5 Conversao e Ajuste de Casas Decimais
            //Como o formato eh 000,00 (2 casas decimais para litros), dividimos por 100.
            //Exemplo: Leitura "00102" -> 102 -> 1.02 m3
            double valorFinal = Double.parseDouble(melhorCandidato) / 100.0;

            return valorFinal;

        } catch (IOException e) {
            throw new LeituraException("Erro de IO ao ler arquivo", e);
        } catch (TesseractException e) {
            throw new LeituraException("Erro interno na biblioteca Tesseract", e);
        } catch (NumberFormatException e) {
            throw new LeituraException("Erro ao converter texto lido em número", e);
        }
    }
}