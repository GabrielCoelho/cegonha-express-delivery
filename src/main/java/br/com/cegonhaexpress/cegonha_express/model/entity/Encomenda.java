package br.com.cegonhaexpress.cegonha_express.model.entity;

import br.com.cegonhaexpress.cegonha_express.model.base.BaseEntity;
import br.com.cegonhaexpress.cegonha_express.model.enums.StatusEncomenda;
import br.com.cegonhaexpress.cegonha_express.model.enums.TipoEntrega;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "encomendas",
    indexes = {
      @Index(name = "idx_encomenda_codigo", columnList = "codigo", unique = true),
      @Index(name = "idx_encomenda_cliente", columnList = "cliente_id"),
      @Index(name = "idx_encomenda_status", columnList = "status"),
      @Index(name = "idx_encomenda_data_criacao", columnList = "data_pedido")
    })
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(
    callSuper = true,
    exclude = {"cliente", "enderecoOrigem", "enderecoDestino", "frete"})
public class Encomenda extends BaseEntity {

  @NotBlank(message = "Código da encomenda é obrigatório")
  @Size(max = 20, message = "Código pode ter até 20 caracteres")
  @Column(name = "codigo", nullable = false, unique = true, length = 20)
  private String codigo;

  @NotNull(message = "Cliente é obrigatório")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @NotNull(message = "Endereco de Origem é obrigatório")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "endereco_origem_id", nullable = false)
  private Endereco enderecoOrigem;

  @NotNull(message = "Endereco de Destino é obrigatório")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "endereco_destino_id", nullable = false)
  private Endereco enderecoDestino;

  @NotNull(message = "Tipo de Entrega é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_entrega", nullable = false, length = 20)
  private TipoEntrega tipoEntrega;

  @NotNull(message = "Status da Encomenda é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private StatusEncomenda status;

  @NotNull(message = "Data do pedido é obrigatória")
  @Column(name = "data_pedido", nullable = false)
  private LocalDateTime dataPedido;

  @Column(name = "data_estimada_entrega")
  private LocalDate dataEstimadaEntrega;

  @Column(name = "data_entrega_realizada")
  private LocalDateTime dataEntregaRealizada;

  @NotBlank(message = "Descrição da Encomenda (bebê) é obrigatória")
  @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
  @Column(name = "descricao_bebe", nullable = false, length = 500)
  private String descricaoBebe;

  @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0Kg")
  @DecimalMax(value = "15.0", message = "Peso deve ser menor que 15Kg")
  @Column(name = "peso_kg", precision = 5, scale = 2)
  private BigDecimal pesoKg;

  @DecimalMin(value = "20.0", message = "Altura deve ser maior que 20cm")
  @DecimalMax(value = "100.0", message = "Altura deve ser menor que 100cm")
  @Column(name = "altura_cm", precision = 5, scale = 2)
  private BigDecimal alturaCm;

  @DecimalMin(value = "0.0", message = "Valor declarado não pode ser negativo")
  @Column(name = "valor_declarado", precision = 10, scale = 2)
  private BigDecimal valorDeclarado;

  @Size(max = 1000, message = "Observações pode ter até 1000 caracteres")
  @Column(name = "observacoes", length = 1000)
  private String observacoes;

  @OneToOne(mappedBy = "encomenda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Frete frete;

  /**
   * Construtor para criação de encomenda com dados essenciais.
   *
   * @param cliente Cliente que fez o pedido
   * @param enderecoOrigem Endereço de coleta
   * @param enderecoDestino Endereço de entrega
   * @param tipoEntrega Modalidade de entrega
   * @param descricaoBebe Descrição do bebê reborn
   */
  public Encomenda(
      Cliente cliente,
      Endereco enderecoOrigem,
      Endereco enderecoDestino,
      TipoEntrega tipoEntrega,
      String descricaoBebe) {
    this.cliente = cliente;
    this.enderecoOrigem = enderecoOrigem;
    this.enderecoDestino = enderecoDestino;
    this.tipoEntrega = tipoEntrega;
    this.descricaoBebe = descricaoBebe;
    this.dataPedido = LocalDateTime.now();
    this.status = StatusEncomenda.PENDENTE;
    this.codigo = gerarCodigoUnico();
  }

  /**
   * Construtor completo com dimensões e valor.
   *
   * @param cliente Cliente que fez o pedido
   * @param enderecoOrigem Endereço de coleta
   * @param enderecoDestino Endereço de entrega
   * @param tipoEntrega Modalidade de entrega
   * @param descricaoBebe Descrição do bebê reborn
   * @param pesoKg Peso em quilogramas
   * @param alturaCm Altura em centímetros
   * @param valorDeclarado Valor declarado para seguro
   */
  public Encomenda(
      Cliente cliente,
      Endereco enderecoOrigem,
      Endereco enderecoDestino,
      TipoEntrega tipoEntrega,
      String descricaoBebe,
      BigDecimal pesoKg,
      BigDecimal alturaCm,
      BigDecimal valorDeclarado) {
    this(cliente, enderecoOrigem, enderecoDestino, tipoEntrega, descricaoBebe);
    this.pesoKg = pesoKg;
    this.alturaCm = alturaCm;
    this.valorDeclarado = valorDeclarado;
  }

  // ==================== MÉTODOS DE NEGÓCIO ====================

  /**
   * Confirma a encomenda, mudando status para CONFIRMADA. Calcula automaticamente a data estimada
   * de entrega.
   */
  public void confirmar() {
    if (this.status != StatusEncomenda.PENDENTE) {
      throw new IllegalStateException("Só é possível confirmar encomendas pendentes");
    }

    this.status = StatusEncomenda.CONFIRMADA;
    this.dataEstimadaEntrega = calcularDataEstimadaEntrega();
  }

  /** Marca a encomenda como em trânsito. */
  public void iniciarTransito() {
    if (this.status != StatusEncomenda.CONFIRMADA) {
      throw new IllegalStateException("Só é possível iniciar trânsito de encomendas confirmadas");
    }

    this.status = StatusEncomenda.EM_TRANSITO;
  }

  /** Finaliza a entrega, registrando data/hora de entrega. */
  public void finalizarEntrega() {
    if (this.status != StatusEncomenda.EM_TRANSITO) {
      throw new IllegalStateException("Só é possível finalizar encomendas em trânsito");
    }

    this.status = StatusEncomenda.ENTREGUE;
    this.dataEntregaRealizada = LocalDateTime.now();
  }

  /**
   * Cancela a encomenda se ainda não estiver em trânsito.
   *
   * @param motivo Motivo do cancelamento
   */
  public void cancelar(String motivo) {
    if (this.status == StatusEncomenda.ENTREGUE) {
      throw new IllegalStateException("Não é possível cancelar encomendas já entregues");
    }

    this.status = StatusEncomenda.CANCELADA;
    if (motivo != null && !motivo.trim().isEmpty()) {
      this.observacoes =
          (this.observacoes != null ? this.observacoes + "\n" : "") + "CANCELAMENTO: " + motivo;
    }
  }

  // ==================== MÉTODOS UTILITÁRIOS ====================

  /**
   * Verifica se a encomenda pode ser modificada.
   *
   * @return true se status permite modificação
   */
  public boolean podeSerModificada() {
    return status == StatusEncomenda.PENDENTE;
  }

  /**
   * Verifica se a encomenda está ativa (não cancelada).
   *
   * @return true se não está cancelada
   */
  public boolean isAtiva() {
    return status != StatusEncomenda.CANCELADA;
  }

  /**
   * Verifica se a entrega foi realizada.
   *
   * @return true se status é ENTREGUE
   */
  public boolean isEntregue() {
    return status == StatusEncomenda.ENTREGUE;
  }

  /**
   * Verifica se a encomenda está atrasada.
   *
   * @return true se passou da data estimada e não foi entregue
   */
  public boolean isAtrasada() {
    if (dataEstimadaEntrega == null || isEntregue()) {
      return false;
    }
    return LocalDate.now().isAfter(dataEstimadaEntrega);
  }

  /**
   * Calcula se é entrega interestadual.
   *
   * @return true se origem e destino são estados diferentes
   */
  public boolean isEntregaInterestadual() {
    return enderecoOrigem != null
        && enderecoDestino != null
        && !enderecoOrigem.getUf().equals(enderecoDestino.getUf());
  }

  /**
   * Retorna o valor total da encomenda (frete + valor declarado).
   *
   * @return valor total ou null se frete não calculado
   */
  public BigDecimal getValorTotal() {
    if (frete == null) {
      return null;
    }

    BigDecimal valorFrete = frete.getValor();
    BigDecimal valorDecl = valorDeclarado != null ? valorDeclarado : BigDecimal.ZERO;

    return valorFrete.add(valorDecl);
  }

  /**
   * Retorna descrição resumida da encomenda.
   *
   * @return resumo formatado
   */
  public String getResumo() {
    return String.format(
        "%s - %s → %s (%s)",
        codigo,
        enderecoOrigem != null ? enderecoOrigem.getCidade() : "Origem",
        enderecoDestino != null ? enderecoDestino.getCidade() : "Destino",
        status.getDescricao());
  }

  // ==================== MÉTODOS PRIVADOS ====================

  /**
   * Gera código único para a encomenda. Formato: CE + timestamp + sufixo aleatório
   *
   * @return código único
   */
  private String gerarCodigoUnico() {
    long timestamp = System.currentTimeMillis();
    int sufixo = (int) (Math.random() * 1000);
    return String.format("CE%d%03d", timestamp % 1000000, sufixo);
  }

  /**
   * Calcula data estimada de entrega baseada no tipo.
   *
   * @return data estimada
   */
  private LocalDate calcularDataEstimadaEntrega() {
    if (tipoEntrega == null) {
      return LocalDate.now().plusDays(7); // Padrão
    }

    LocalDate hoje = LocalDate.now();
    return switch (tipoEntrega) {
      case EXPRESSA -> hoje.plusDays(1);
      case PADRAO -> hoje.plusDays(3);
      case ECONOMICA -> hoje.plusDays(7);
    };
  }

  // ==================== CALLBACKS JPA ====================

  /** Callback executado antes da persistência. Inicializa campos obrigatórios se não definidos. */
  @PrePersist
  public void onCreate() {
    if (dataPedido == null) {
      dataPedido = LocalDateTime.now();
    }
    if (status == null) {
      status = StatusEncomenda.PENDENTE;
    }
    if (codigo == null || codigo.trim().isEmpty()) {
      codigo = gerarCodigoUnico();
    }
  }

  /** Callback executado antes de atualizações. Valida transições de status. */
  @PreUpdate
  public void onUpdate() {
    // Validações adicionais podem ser implementadas aqui
    if (observacoes != null) {
      observacoes = observacoes.trim();
      if (observacoes.isEmpty()) {
        observacoes = null;
      }
    }
  }
}
