package br.ifpb.monitoramento.adapter;

import java.io.File;

public interface ILeitorImagem {
    //Metodo definido no diagrama de classes
    double extrairValor(File arquivo) throws LeituraException;
}