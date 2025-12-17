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


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoramentoFacade {

    //1 Variavel estatica para guardar a unica instância (SINGLETON)
    private static MonitoramentoFacade instance;

    //private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final UsuarioDAO usuarioDAO;
    private final ILeitorImagem leitorOCR; // Componente de OCR
    private final List<IObservadorAlerta> observadores = new ArrayList<>();
    private ScheduledExecutorService scheduler;

    //2 Construtor PRIVADO para impedir 'new MonitoramentoFacade()' fora daqui
    private MonitoramentoFacade() {
        this.usuarioDAO = new UsuarioArquivoDAO();
        this.leitorOCR = new TesseractAdapter();
        this.observadores.add(new EmailNotificador());
    }

    //3 Metodo publico estático para acessar a instancia (Ponto Global de Acesso)
    public static synchronized MonitoramentoFacade getInstance() {
        if (instance == null) {
            instance = new MonitoramentoFacade();
        }
        return instance;
    }

    public void iniciarMonitoramento() {
        if (scheduler != null && !scheduler.isShutdown()) {
            System.out.println(">>> O monitoramento já está rodando.");
            return;
        }

        System.out.println(">>> Painel de Monitoramento Iniciado <<<");
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::verificarDiretorios, 0, 5, TimeUnit.SECONDS);
    }

    public void pararMonitoramento() {
        if (scheduler != null) {
            scheduler.shutdown();
            System.out.println(">>> Monitoramento Parado <<<");
        }
    }

    private void verificarDiretorios() {
        //System.out.println("\n[AGENDADOR] Buscando hidrometros ativos no banco...");
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        for (Usuario u : usuarios) {
            for (Hidrometro h : u.getHidrometros()) {
                if (h.isAtivo()) {
                    monitorarPasta(h, u);
                }
            }
        }
    }

    private void monitorarPasta(Hidrometro sha, Usuario usuario) {
        File diretorio = new File(sha.getDiretorioMonitorado());
        if (!diretorio.exists() || !diretorio.isDirectory()) return;

        File[] arquivos = diretorio.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));

        if (arquivos != null && arquivos.length > 0) {
            java.util.Arrays.sort(arquivos, java.util.Comparator.comparing(File::getName));

            for (File arquivo : arquivos) {
                System.out.println("⚠️ NOVA IMAGEM DETECTADA: " + arquivo.getName());
                try {
                    double valorLido = leitorOCR.extrairValor(arquivo);
                    double consumo = sha.calcularConsumo(valorLido);
                    sha.registrarLeitura(new Leitura(valorLido, arquivo.getName()));
                    usuarioDAO.atualizar(usuario);

                    System.out.println("   ✅ Leitura: " + valorLido + " | Consumo da leitura: " + consumo);

                    if (valorLido > usuario.getLimiteConsumoMensal()) {
                        System.out.println("   🚨 ALERTA: Limite ultrapassado!");
                        notificarObservadores(usuario, valorLido);
                    }
                    moverArquivo(diretorio, arquivo, "processados");
                } catch (LeituraException e) {
                    System.err.println("   ❌ Falha no OCR: " + e.getMessage());
                    moverArquivo(diretorio, arquivo, "erros_leitura");
                }
            }
        }
    }

    private void notificarObservadores(Usuario usuario, double consumo) {
        for (IObservadorAlerta obs : observadores) {
            obs.notificar(usuario, consumo);
        }
    }

    //Metodo auxiliar generico para mover arquivos
    private void moverArquivo(File diretorioPai, File arquivoOriginal, String nomePastaDestino) {
        File pastaDestino = new File(diretorioPai, nomePastaDestino);
        if (!pastaDestino.exists()) {
            pastaDestino.mkdir();
        }
        File destino = new File(pastaDestino, arquivoOriginal.getName());
        if (destino.exists()) {
            destino = new File(pastaDestino, System.currentTimeMillis() + "_" + arquivoOriginal.getName());
        }
        if (arquivoOriginal.renameTo(destino)) {
            System.out.println("   📂 Arquivo movido para: " + destino.getAbsolutePath());
        }
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }
}