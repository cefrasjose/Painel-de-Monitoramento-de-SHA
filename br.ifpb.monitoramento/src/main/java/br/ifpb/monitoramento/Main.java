package br.ifpb.monitoramento;

import br.ifpb.monitoramento.facade.MonitoramentoFacade;
import br.ifpb.monitoramento.model.Hidrometro;
import br.ifpb.monitoramento.model.TipoUsuario;
import br.ifpb.monitoramento.model.Usuario;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MonitoramentoFacade painel = new MonitoramentoFacade();

        //DADOS INICIAIS
        //Se o banco estiver vazio, cria um usuario padrao com os SHAs
        if (painel.getUsuarioDAO().listarTodos().isEmpty()) {
            System.out.println("Banco vazio. Criando usuario de teste...");

            Usuario user = new Usuario("Cefras Jose", "123.456.789-00", "cefras@email.com", TipoUsuario.ADMIN);

            //Caminhos dos SHAs
            Hidrometro sha1 = new Hidrometro
                    ("SHA-001", "C:\\Users\\cefra\\Documents\\simulador hidrometro\\Medições_202021250024");
            Hidrometro sha2 = new Hidrometro
                    ("SHA-002", "C:\\Users\\cefra\\Documents\\arthur_hidrometro\\Medições_202310980012");

            user.adicionarHidrometro(sha1);
            user.adicionarHidrometro(sha2);

            painel.getUsuarioDAO().salvar(user);
            System.out.println("Usuario criado com sucesso no arquivo JSON!");
        }

        painel.iniciarMonitoramento();

        System.out.println("\nPressione ENTER para encerrar...");
        new Scanner(System.in).nextLine();

        painel.pararMonitoramento();
    }
}