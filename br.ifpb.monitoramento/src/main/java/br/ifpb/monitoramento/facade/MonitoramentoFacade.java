package br.ifpb.monitoramento.facade;

import br.ifpb.monitoramento.adapter.ILeitorImagem;
import br.ifpb.monitoramento.adapter.LeituraException;
import br.ifpb.monitoramento.dao.UsuarioArquivoDAO;
import br.ifpb.monitoramento.adapter.TesseractAdapter;
import br.ifpb.monitoramento.dao.UsuarioDAO;
import br.ifpb.monitoramento.model.Hidrometro;
import br.ifpb.monitoramento.model.Leitura;
import br.ifpb.monitoramento.model.Usuario;
import br.ifpb.monitoramento.observer.EmailNotificador;
import br.ifpb.monitoramento.observer.IObservadorAlerta;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoramentoFacade {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final UsuarioDAO usuarioDAO;
    private final ILeitorImagem leitorOCR; // Componente de OCR
    private final List<IObservadorAlerta> observadores = new ArrayList<>();

    public MonitoramentoFacade() {
        this.usuarioDAO = new UsuarioArquivoDAO();
        this.leitorOCR = new TesseractAdapter(); //Injecao da implementacao concreta (Tesseract)
        //Registra o Notificador de Email (Padrao Observer)
        this.observadores.add(new EmailNotificador());
    }

    public void iniciarMonitoramento() {
        System.out.println(">>> Painel de Monitoramento Iniciado <<<");
        scheduler.scheduleAtFixedRate(this::verificarDiretorios, 0, 5, TimeUnit.SECONDS);
    }

    public void pararMonitoramento() {
        scheduler.shutdown();
        System.out.println(">>> Monitoramento Parado <<<");
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    private void verificarDiretorios() {
        //System.out.println("\n[AGENDADOR] Buscando hidrometros ativos no banco...");

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        if (usuarios.isEmpty()) return;

        for (Usuario usuario : usuarios) {
            for (Hidrometro sha : usuario.getHidrometros()) {
                if (sha.isAtivo()) {
                    monitorarPasta(sha, usuario);
                }
            }
        }
    }

    private void monitorarPasta(Hidrometro sha, Usuario usuario) {
        File diretorio = new File(sha.getDiretorioMonitorado());

        if (diretorio.exists() && diretorio.isDirectory()) {
            // Busca apenas JPG/JPEG
            File[] arquivos = diretorio.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));

            if (arquivos != null && arquivos.length > 0) {

                java.util.Arrays.sort(arquivos, java.util.Comparator.comparing(File::getName));

                for (File arquivo : arquivos) {
                    System.out.println("⚠️ NOVA IMAGEM DETECTADA: " + arquivo.getName());

                    try {
                        // 1. OCR (Agora corrigido)
                        double valorLido = leitorOCR.extrairValor(arquivo);

                        // 2. Calcula Consumo
                        double consumo = sha.calcularConsumo(valorLido);
                        sha.registrarLeitura(new Leitura(valorLido, arquivo.getName()));

                        // 3. Atualiza JSON
                        usuarioDAO.atualizar(usuario);

                        System.out.println("   ✅ Leitura: " + valorLido + " | Consumo da leitura: " + consumo);

                        // --- NOVO: VERIFICAÇÃO DE ALERTA (RF-A01) ---
                        // Aqui você pode somar o histórico ou usar o consumo pontual.
                        // Exemplo simplificado: Se a leitura atual for muito alta ou passar do limite do usuário
                        if (valorLido > usuario.getLimiteConsumoMensal()) {
                            System.out.println("   🚨 ALERTA: Limite ultrapassado!");
                            notificarObservadores(usuario, valorLido);
                        }
                        // --------------------------------------------

                        moverArquivo(diretorio, arquivo, "processados");

                    } catch (LeituraException e) {
                        System.err.println("   ❌ Falha no OCR: " + e.getMessage());
                        moverArquivo(diretorio, arquivo, "erros_leitura");
                    }
                }
            }
        }
    }

    private void notificarObservadores(Usuario usuario, double consumo) {
        for (IObservadorAlerta obs : observadores) {
            obs.notificar(usuario, consumo);
        }
    }

    // Metodo auxiliar generico para mover arquivos
    private void moverArquivo(File diretorioPai, File arquivoOriginal, String nomePastaDestino) {
        File pastaDestino = new File(diretorioPai, nomePastaDestino);
        if (!pastaDestino.exists()) {
            pastaDestino.mkdir();
        }

        File destino = new File(pastaDestino, arquivoOriginal.getName());

        // Se ja existir um arquivo com mesmo nome na pasta destino, tenta renomear
        if (destino.exists()) {
            destino = new File(pastaDestino, System.currentTimeMillis() + "_" + arquivoOriginal.getName());
        }

        if (arquivoOriginal.renameTo(destino)) {
            System.out.println("   📂 Arquivo movido para: " + destino.getAbsolutePath());
        } else {
            System.err.println("   ⚠️ Erro crítico: Não foi possível mover o arquivo " + arquivoOriginal.getName());
        }
    }

    private void moverParaProcessados(File diretorioPai, File arquivoOriginal) {
        File pastaProcessados = new File(diretorioPai, "processados");
        if (!pastaProcessados.exists()) {
            pastaProcessados.mkdir();
        }

        File destino = new File(pastaProcessados, arquivoOriginal.getName());
        if (arquivoOriginal.renameTo(destino)) {
            System.out.println("   📂 Arquivo movido para: " + destino.getAbsolutePath());
        } else {
            System.err.println("   ⚠️ Erro ao mover arquivo. Verifique permissões.");
        }
    }
}