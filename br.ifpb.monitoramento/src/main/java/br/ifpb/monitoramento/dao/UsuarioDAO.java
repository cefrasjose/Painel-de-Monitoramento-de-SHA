package br.ifpb.monitoramento.dao;

import br.ifpb.monitoramento.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void salvar(Usuario usuario);
    Usuario buscarPorCpf(String cpf);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
    //deletar se necessario
}