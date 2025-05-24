package br.com.cegonhaexpress.cegonha_express.model.enums;

public enum StatusEncomenda {
  PENDENTE("Pendente"),
  CONFIRMADA("Confirmada"),
  EM_TRANSITO("Em Trânsito"),
  ENTREGUE("Entregue"),
  CANCELADA("Cancelada");

  private final String descricao;

  StatusEncomenda(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
