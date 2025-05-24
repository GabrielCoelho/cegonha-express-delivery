package br.com.cegonhaexpress.cegonha_express.model.enums;

public enum TipoEntrega {
  EXPRESSA("Expressa", 1),
  PADRAO("Padrão", 3),
  ECONOMICA("Economica", 7);

  private final String descricao;
  private final int diasMinimosEntrega;

  TipoEntrega(String descricao, int diasEntrega) {
    this.descricao = descricao;
    this.diasMinimosEntrega = diasEntrega;
  }

  public String getDescricao() {
    return descricao;
  }

  public int getDiasMinimosEntrega() {
    return diasMinimosEntrega;
  }
}
