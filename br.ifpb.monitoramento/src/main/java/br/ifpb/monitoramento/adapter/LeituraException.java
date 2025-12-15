package br.ifpb.monitoramento.adapter;

public class LeituraException extends Exception {
  public LeituraException(String message, Throwable cause) {
    super(message, cause);
  }

  public LeituraException(String message) {
    super(message);
  }
}