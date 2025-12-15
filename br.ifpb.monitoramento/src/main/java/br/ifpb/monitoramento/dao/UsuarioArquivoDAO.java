package br.ifpb.monitoramento.dao;

import br.ifpb.monitoramento.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsuarioArquivoDAO implements UsuarioDAO {

    private final String CAMINHO_ARQUIVO = "banco_usuarios.json";
    private Gson gson;

    public UsuarioArquivoDAO() {
        //Registramos o adaptador para ensinar o GSON a lidar com LocalDateTime sem usar Reflection
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTime())
                .create();

        verificarArquivo();
    }

    private void verificarArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
                salvarNoArquivo(new ArrayList<>()); //Inicia com lista vazia
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Le tudo do arquivo para memoria
    private List<Usuario> carregarDoArquivo() {
        try (Reader reader = new FileReader(CAMINHO_ARQUIVO)) {
            Type listType = new TypeToken<ArrayList<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, listType);
            return usuarios == null ? new ArrayList<>() : usuarios;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //Escreve a lista atualizada no arquivo
    private void salvarNoArquivo(List<Usuario> usuarios) {
        try (Writer writer = new FileWriter(CAMINHO_ARQUIVO)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void salvar(Usuario usuario) {
        List<Usuario> lista = carregarDoArquivo();
        lista.add(usuario);
        salvarNoArquivo(lista);
    }

    @Override
    public Usuario buscarPorCpf(String cpf) {
        List<Usuario> lista = carregarDoArquivo();
        for (Usuario u : lista) {
            if (u.getCpf().equals(cpf)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        return carregarDoArquivo();
    }

    @Override
    public void atualizar(Usuario usuarioAtualizado) {
        List<Usuario> lista = carregarDoArquivo();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCpf().equals(usuarioAtualizado.getCpf())) {
                lista.set(i, usuarioAtualizado); //Substitui
                break;
            }
        }
        salvarNoArquivo(lista);
    }
}