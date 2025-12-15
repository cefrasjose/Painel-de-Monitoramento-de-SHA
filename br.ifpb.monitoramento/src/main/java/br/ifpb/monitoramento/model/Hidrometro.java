package br.ifpb.monitoramento.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Hidrometro {
    private String id;
    private String diretorioMonitorado; //Caminho da pasta do SHA (C:/...)
    private double ultimaLeitura;
    private boolean ativo;
    private List<Leitura> historicoLeituras;

    public Hidrometro(String id, String diretorioMonitorado) {
        this.id = id;
        this.diretorioMonitorado = diretorioMonitorado;
        this.ativo = true;
        this.ultimaLeitura = 0.0;
        this.historicoLeituras = new ArrayList<>();
    }

    public void registrarLeitura(Leitura leitura) {
        this.historicoLeituras.add(leitura);
        this.ultimaLeitura = leitura.getValor();
    }

    //Metodo auxiliar para pegar o consumo (Leitura Atual - Anterior)
    //Se nao tiver anterior, consumo eh 0 ou o proprio valor.
    public double calcularConsumo(double novaLeitura) {
        if (ultimaLeitura == 0.0) return 0.0; //Primeira leitura

        //2 Protecao contra consumo negativo (Reset do hidrometro ou erro de OCR)
        if (novaLeitura < ultimaLeitura) {
            System.out.println("   🔄 [INFO] Leitura menor que a anterior (Reset detectado). Reiniciando contagem.");
            return 0.0; // Assume consumo zero neste ciclo para não gerar negativo
        }
        return novaLeitura - ultimaLeitura;
    }

    //Getters e Setters
    public String getId() { return id; }
    public String getDiretorioMonitorado() { return diretorioMonitorado; }
    public double getUltimaLeitura() { return ultimaLeitura; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public List<Leitura> getHistoricoLeituras() { return historicoLeituras; }
}