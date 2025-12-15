package br.ifpb.monitoramento.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private String cpf; //Sera a chave primaria
    private String email;
    private TipoUsuario tipo; //ADMIN ou CLIENTE
    private List<Hidrometro> hidrometros;
    private double limiteConsumoMensal; //Para o Alerta

    public Usuario(String nome, String cpf, String email, TipoUsuario tipo) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.tipo = tipo;
        this.hidrometros = new ArrayList<>();
        this.limiteConsumoMensal = 100.0; //Default: 100 m3/litros
    }

    public void adicionarHidrometro(Hidrometro h) {
        this.hidrometros.add(h);
    }

    // Getters e Setters
    public String getCpf() { return cpf; }
    public List<Hidrometro> getHidrometros() { return hidrometros; }
    public String getEmail() { return email; }
    public double getLimiteConsumoMensal() { return limiteConsumoMensal; }
    public String getNome() { return nome; }
}