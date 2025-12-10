package br.ifpb.monitoramento;

import br.ifpb.monitoramento.facade.MonitoramentoFacade;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MonitoramentoFacade painel = new MonitoramentoFacade();

        //Inicia a thread de monitoramento
        painel.iniciarMonitoramento();

        //Mantem a aplicacao rodando ate o usuario apertar Enter
        System.out.println("\nPressione ENTER para encerrar o sistema...");
        new Scanner(System.in).nextLine();

        painel.pararMonitoramento();
    }
}