package br.ifpb.monitoramento.model;

public class UsuarioFactory {

    //Metodo de Fabrica (Static Factory Method)
    //Encapsula a logica de criacao. Se amanha o sistema tiver tipos diferentes (ex: UsuarioAdmin, UsuarioVisitante), alteramos aqui e nao no Menu.
    public static Usuario criarUsuarioPadrao(String nome, String cpf, String email) {
        //Define que por padrao todo novo cadastro via menu e do tipo CLIENTE
        return new Usuario(nome, cpf, email, TipoUsuario.CLIENTE);
    }
}