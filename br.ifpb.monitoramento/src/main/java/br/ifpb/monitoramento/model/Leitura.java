package br.ifpb.monitoramento.model;

import java.time.LocalDateTime;

public class Leitura {
    private double valor;
    private LocalDateTime dataHora;
    private String nomeArquivo;

    public Leitura(double valor, String nomeArquivo) {
        this.valor = valor;
        this.nomeArquivo = nomeArquivo;
        this.dataHora = LocalDateTime.now();
    }

    //Getters e Setters
    public double getValor() { return valor; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getNomeArquivo() { return nomeArquivo; }

    @Override
    public String toString() {
        return "Leitura{" + "valor=" + valor + ", data=" + dataHora + '}';
    }
}