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
        //Configura o caminho relativo para a pasta na raiz do projeto
        this.tesseract.setDatapath("tessdata");
        this.tesseract.setLanguage("eng");

        //Configuracao vital: Força o Tesseract a considerar apenas numeros
        this.tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");
    }

    @Override
    public double extrairValor(File arquivo) throws LeituraException {
        try {
            //1 Carrega a imagem para a memoria (libera o arquivo do disco imediatamente)
            BufferedImage imagemOriginal = ImageIO.read(arquivo);
            if (imagemOriginal == null) {
                throw new LeituraException("Arquivo de imagem inválido ou corrompido.");
            }

            //2 Pre-processamento: Ampliar e converter para Escala de Cinza
            int novaLargura = imagemOriginal.getWidth() * 2;
            int novaAltura = imagemOriginal.getHeight() * 2;

            BufferedImage imagemTratada = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = imagemTratada.createGraphics();

            //Interpolacao de alta qualidade para nao distorcer os numeros ao ampliar
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(imagemOriginal, 0, 0, novaLargura, novaAltura, null);
            g.dispose();

            //3 Executa o OCR na imagem tratada
            String texto = tesseract.doOCR(imagemTratada);

            //4 Logica de Filtro (Correcao para Lixo de OCR)
            //Divide o texto encontrado por espaços ou quebras de linha
            String[] possiveisNumeros = texto.split("\\s+");
            String melhorCandidato = "";

            for (String s : possiveisNumeros) {
                //Garante que so temos digitos
                String limpo = s.replaceAll("[^0-9]", "");

                //Regra de Negocio: Um hidrometro comum dificilmente tem mais de 8 digitos ativos
                if (!limpo.isEmpty() && limpo.length() <= 8) {
                    //Pega a sequencia numerica mais longa encontrada que respeite o limite
                    if (limpo.length() > melhorCandidato.length()) {
                        melhorCandidato = limpo;
                    }
                }
            }

            System.out.println("   [OCR] Texto Bruto: [" + texto.replace("\n", " | ").trim() + "]");
            System.out.println("   [OCR] Melhor candidato validado: [" + melhorCandidato + "]");

            if (melhorCandidato.isEmpty()) {
                throw new LeituraException("Nenhum padrao numerico valido encontrado na imagem.");
            }

            return Double.parseDouble(melhorCandidato);

        } catch (IOException e) {
            throw new LeituraException("Erro de IO ao ler arquivo", e);
        } catch (TesseractException e) {
            throw new LeituraException("Erro interno na biblioteca Tesseract", e);
        } catch (NumberFormatException e) {
            throw new LeituraException("Erro ao converter texto lido em número", e);
        }
    }
}