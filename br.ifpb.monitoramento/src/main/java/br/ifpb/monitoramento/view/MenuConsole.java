package br.ifpb.monitoramento.view;

import br.ifpb.monitoramento.facade.MonitoramentoFacade;
import br.ifpb.monitoramento.model.Hidrometro;
import br.ifpb.monitoramento.model.Leitura;
import br.ifpb.monitoramento.model.TipoUsuario;
import br.ifpb.monitoramento.model.Usuario;

import java.util.Scanner;

public class MenuConsole {

    private final MonitoramentoFacade facade;
    private final Scanner scanner;

    public MenuConsole() {
        this.facade = new MonitoramentoFacade();
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== 💧 PAINEL DE MONITORAMENTO CAGEPA (Simulado) ===");
            System.out.println("1. Cadastrar Novo Usuário");
            System.out.println("2. Adicionar Hidrômetro a Usuário");
            System.out.println("3. Listar Usuários e Consumos");
            System.out.println("4. Iniciar Monitoramento (Agendador)");
            System.out.println("5. Parar Monitoramento");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> cadastrarUsuario();
                case "2" -> adicionarHidrometro();
                case "3" -> listarDados();
                case "4" -> facade.iniciarMonitoramento();
                case "5" -> facade.pararMonitoramento();
                case "0" -> {
                    facade.pararMonitoramento();
                    System.out.println("Saindo do sistema...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarUsuario() {
        System.out.println("\n--- Cadastro de Usuário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        Usuario u = new Usuario(nome, cpf, email, TipoUsuario.CLIENTE);
        facade.getUsuarioDAO().salvar(u);
        System.out.println("✅ Usuário cadastrado com sucesso!");
    }

    private void adicionarHidrometro() {
        System.out.print("\nDigite o CPF do usuário: ");
        String cpf = scanner.nextLine();
        Usuario u = facade.getUsuarioDAO().buscarPorCpf(cpf);

        if (u == null) {
            System.out.println("❌ Usuario nao encontrado.");
            return;
        }

        System.out.print("ID do Hidrometro (ex: SHA-01): ");
        String id = scanner.nextLine();
        System.out.print("Caminho da Pasta (ex: C:/temp/sha01): ");
        String caminho = scanner.nextLine();

        Hidrometro h = new Hidrometro(id, caminho);
        u.adicionarHidrometro(h);
        facade.getUsuarioDAO().atualizar(u);
        System.out.println("✅ Hidrometro vinculado com sucesso!");
    }

    private void listarDados() {
        System.out.println("\n--- Relatorio de Consumo ---");
        for (Usuario u : facade.getUsuarioDAO().listarTodos()) {
            System.out.println("👤 " + u.getNome() + " (CPF: " + u.getCpf() + ")");
            for (Hidrometro h : u.getHidrometros()) {
                System.out.println("   SHA: " + h.getId() + " | Caminho: " + h.getDiretorioMonitorado());
                System.out.println("   Ultima Leitura: " + h.getUltimaLeitura() + " m3");
                if (!h.getHistoricoLeituras().isEmpty()) {
                    System.out.println("   📝 Historico Recente:");
                    for (Leitura l : h.getHistoricoLeituras()) {
                        System.out.println("      - " + l.getDataHora() + ": " + l.getValor());
                    }
                }
            }
            System.out.println("-------------------------");
        }
    }
}