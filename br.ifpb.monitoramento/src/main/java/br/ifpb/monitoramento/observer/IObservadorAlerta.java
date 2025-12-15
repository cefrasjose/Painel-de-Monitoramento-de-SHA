package br.ifpb.monitoramento.observer;

import br.ifpb.monitoramento.model.Usuario;

public interface IObservadorAlerta {
    void notificar(Usuario usuario, double consumoAtual);
}