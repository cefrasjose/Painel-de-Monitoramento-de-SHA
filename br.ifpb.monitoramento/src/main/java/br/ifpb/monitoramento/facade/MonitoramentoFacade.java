package br.ifpb.monitoramento.facade;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoramentoFacade {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //Defina aqui os caminhos das pastas dos SHAs
    private final String[] diretoriosSHA = {
            "C:\\Users\\cefra\\Documents\\simulador hidrometro\\Medições_202021250024",
            "C:\\Users\\cefra\\Documents\\arthur_hidrometro\\Medições_202310980012"
    };

    public void iniciarMonitoramento() {
        System.out.println(">>> Painel de Monitoramento Iniciado <<<");
        System.out.println("Monitorando diretórios: ");
        for (String dir : diretoriosSHA) {
            System.out.println(" - " + dir);
        }

        //Agendador: Roda a tarefa 'verificarDiretorios' a cada 5 segundos
        scheduler.scheduleAtFixedRate(this::verificarDiretorios, 0, 5, TimeUnit.SECONDS);
    }

    public void pararMonitoramento() {
        scheduler.shutdown();
        System.out.println(">>> Monitoramento Parado <<<");
    }

    private void verificarDiretorios() {
        System.out.println("\n[AGENDADOR] Verificando novos arquivos nos SHAs...");

        for (String caminhoDir : diretoriosSHA) {
            File diretorio = new File(caminhoDir);

            //Se o diretorio existe e eh uma pasta
            if (diretorio.exists() && diretorio.isDirectory()) {
                File[] arquivos = diretorio.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));

                if (arquivos != null && arquivos.length > 0) {
                    for (File arquivo : arquivos) {
                        System.out.println("⚠️ ALERTA: Nova leitura detectada!");
                        System.out.println("   SHA Origem: " + caminhoDir);
                        System.out.println("   Arquivo: " + arquivo.getName());

                        File processado = new File(diretorio, arquivo.getName() + ".processed");
                        arquivo.renameTo(processado);
                        System.out.println("   Status: Arquivo marcado como processado.");
                    }
                }
            } else {
                System.err.println("Erro: Diretório não encontrado -> " + caminhoDir);
            }
        }
    }
}