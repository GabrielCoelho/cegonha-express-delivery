package br.com.cegonhaexpress.cegonha_express.model.entity;

import br.com.cegonhaexpress.cegonha_express.model.base.BaseEntity;
import br.com.cegonhaexpress.cegonha_express.model.enums.TipoEntrega;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "fretes",
    indexes = {
      @Index(name = "idx_frete_encomenda", columnList = "encomenda", unique = true),
      @Index(name = "idx_frete_tipo_entrega", columnList = "tipo_entrega"),
      @Index(name = "idx_frete_data_calculo", columnList = "data_calculo"),
      @Index(name = "idx_frete_valor", columnList = "valor")
    })
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(
    callSuper = true,
    exclude = {"encomenda"})
public class Frete extends BaseEntity {

  @NotNull(message = "A Encomenda é obrigatória")
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "encomenda_id", nullable = false, unique = true)
  private Encomenda encomenda;

  @NotNull(message = "Tipo da Entrega é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_entrega", nullable = false, length = 20)
  private TipoEntrega tipoEntrega;

  @NotNull(message = "Valor do frete é obrigatório")
  @DecimalMin(value = "0.00", message = "Valor do frete deve ser maior que 0.00")
  @DecimalMax(value = "600.00", message = "Valor do frete deve ser menor que 600.00")
  @Column(name = "valor", nullable = false, precision = 8, scale = 2)
  private BigDecimal valor;

  @NotNull(message = "Distância é obrigatória")
  @DecimalMin(value = "0.01", message = "Distância em Km deve ser maior que 0.01")
  @DecimalMax(value = "9999.99", message = "Distância em Km deve ser menor que 9999.99")
  @Column(name = "distancia_km", nullable = false, precision = 6, scale = 1)
  private BigDecimal distanciaKm;

  @NotNull(message = "O prazo de entrega é obrigatório")
  @Min(value = 1, message = "O prazo deve ser de ao menos 1 dia")
  @Max(value = 30, message = "O prazo deve ser até 30 dias")
  @Column(name = "prazo_dias", nullable = false)
  private Integer prazoDias;

  @NotNull(message = "Data do Cálculo é obrigatória")
  @Column(name = "data_calculo", nullable = false)
  private LocalDateTime dataCalculo;

  @DecimalMin(value = "0.0", message = "Taxa de distância não pode ser negativa")
  @Column(name = "taxa_distancia", precision = 8, scale = 4)
  private BigDecimal taxaDistancia;

  @DecimalMin(value = "0.0", message = "Taxa de peso não pode ser negativa")
  @Column(name = "taxa_peso", precision = 8, scale = 4)
  private BigDecimal taxaPeso;

  @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
  @Column(name = "observacoes", length = 500)
  private String observacoes;

  @NotBlank(message = "CEP de Origem é obrigatório")
  @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP de origem deve ter formato válido")
  @Column(name = "cep_origem", nullable = false, length = 9)
  private String cepOrigem;

  @NotBlank(message = "CEP de destino é obrigatório")
  @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP de destino deve ter formato válido")
  @Column(name = "cep_origem", nullable = false, length = 9)
  private String cepDestino;

  /**
   * Construtor para criação de frete com dados essenciais.
   *
   * @param encomenda Encomenda associada
   * @param tipoEntrega Tipo de entrega escolhido
   * @param valor Valor calculado do frete
   * @param distanciaKm Distância entre origem e destino
   * @param prazoDias Prazo estimado de entrega
   */
  public Frete(
      Encomenda encomenda,
      TipoEntrega tipoEntrega,
      BigDecimal valor,
      BigDecimal distanciaKm,
      Integer prazoDias) {
    this.encomenda = encomenda;
    this.tipoEntrega = tipoEntrega;
    this.valor = valor;
    this.distanciaKm = distanciaKm;
    this.prazoDias = prazoDias;
    this.dataCalculo = LocalDateTime.now();

    // Extrair CEPs da encomenda se disponível
    if (encomenda != null) {
      if (encomenda.getEnderecoOrigem() != null) {
        this.cepOrigem = encomenda.getEnderecoOrigem().getCep();
      }
      if (encomenda.getEnderecoDestino() != null) {
        this.cepDestino = encomenda.getEnderecoDestino().getCep();
      }
    }
  }

  /**
   * Construtor completo com detalhes do cálculo.
   *
   * @param encomenda Encomenda associada
   * @param tipoEntrega Tipo de entrega
   * @param valor Valor final calculado
   * @param distanciaKm Distância em quilômetros
   * @param prazoDias Prazo em dias
   * @param valorBase Valor base da modalidade
   * @param taxaDistancia Taxa por km
   * @param taxaPeso Taxa por kg
   */
  public Frete(
      Encomenda encomenda,
      TipoEntrega tipoEntrega,
      BigDecimal valor,
      BigDecimal distanciaKm,
      Integer prazoDias,
      BigDecimal valorBase,
      BigDecimal taxaDistancia,
      BigDecimal taxaPeso) {
    this(encomenda, tipoEntrega, valor, distanciaKm, prazoDias);
    this.encomenda.setValorDeclarado(valorBase);
    this.taxaDistancia = taxaDistancia;
    this.taxaPeso = taxaPeso;
  }

  // ==================== MÉTODOS DE NEGÓCIO ====================

  /**
   * Calcula o valor do frete baseado no tipo de entrega e parâmetros. Implementa Strategy Pattern
   * para diferentes modalidades.
   *
   * @param distancia Distância em km
   * @param peso Peso em kg (opcional)
   * @return Valor calculado do frete
   */
  public static BigDecimal calcularFrete(TipoEntrega tipo, BigDecimal distancia, BigDecimal peso) {
    if (tipo == null || distancia == null) {
      throw new IllegalArgumentException("Tipo de entrega e distância são obrigatórios");
    }

    BigDecimal pesoConsiderado = peso != null ? peso : BigDecimal.ONE;

    return switch (tipo) {
      case EXPRESSA -> calcularFreteExpressa(distancia, pesoConsiderado);
      case PADRAO -> calcularFreteStandard(distancia, pesoConsiderado);
      case ECONOMICA -> calcularFreteEconomica(distancia, pesoConsiderado);
    };
  }

  /**
   * Recalcula o frete com novos parâmetros.
   *
   * @param novaDistancia Nova distância
   * @param novoPeso Novo peso
   */
  public void recalcular(BigDecimal novaDistancia, BigDecimal novoPeso) {
    this.distanciaKm = novaDistancia;
    this.encomenda.setPesoKg(novoPeso);
    this.valor = calcularFrete(this.tipoEntrega, novaDistancia, novoPeso);
    this.dataCalculo = LocalDateTime.now();
  }

  /**
   * Verifica se o frete está dentro do prazo estimado.
   *
   * @return true se ainda está no prazo
   */
  public boolean isDentroDoPrazo() {
    if (encomenda == null || encomenda.getDataEstimadaEntrega() == null) {
      return true; // Sem data para comparar
    }
    return !encomenda
        .getDataEstimadaEntrega()
        .isBefore(dataCalculo.toLocalDate().plusDays(prazoDias));
  }

  /**
   * Verifica se é frete interestadual.
   *
   * @return true se origem e destino são estados diferentes
   */
  public boolean isInterestadual() {
    if (encomenda == null) return false;

    return encomenda.getEnderecoOrigem() != null
        && encomenda.getEnderecoDestino() != null
        && !encomenda.getEnderecoOrigem().getUf().equals(encomenda.getEnderecoDestino().getUf());
  }

  /**
   * Calcula o valor por quilômetro.
   *
   * @return valor por km
   */
  public BigDecimal getValorPorKm() {
    if (distanciaKm == null || distanciaKm.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return valor.divide(distanciaKm, 4, RoundingMode.HALF_UP);
  }

  /**
   * Retorna descrição resumida do frete.
   *
   * @return resumo formatado
   */
  public String getResumo() {
    return String.format(
        "%s - R$ %.2f (%d dias) - %.1f km",
        tipoEntrega.getDescricao(), valor, prazoDias, distanciaKm);
  }

  // ==================== MÉTODOS PRIVADOS (Strategy Pattern) ====================

  /** Cálculo específico para entrega expressa. Mais cara, mais rápida. */
  private static BigDecimal calcularFreteExpressa(BigDecimal distancia, BigDecimal peso) {
    BigDecimal valorBase = new BigDecimal("25.00");
    BigDecimal taxaKm = new BigDecimal("1.50");
    BigDecimal taxaPeso = new BigDecimal("2.00");

    BigDecimal valorDistancia = distancia.multiply(taxaKm);
    BigDecimal valorPeso = peso.multiply(taxaPeso);

    return valorBase.add(valorDistancia).add(valorPeso).setScale(2, RoundingMode.HALF_UP);
  }

  /** Cálculo específico para entrega standard. Preço médio, prazo médio. */
  private static BigDecimal calcularFreteStandard(BigDecimal distancia, BigDecimal peso) {
    BigDecimal valorBase = new BigDecimal("15.00");
    BigDecimal taxaKm = new BigDecimal("1.00");
    BigDecimal taxaPeso = new BigDecimal("1.50");

    BigDecimal valorDistancia = distancia.multiply(taxaKm);
    BigDecimal valorPeso = peso.multiply(taxaPeso);

    return valorBase.add(valorDistancia).add(valorPeso).setScale(2, RoundingMode.HALF_UP);
  }

  /** Cálculo específico para entrega econômica. Mais barata, mais demorada. */
  private static BigDecimal calcularFreteEconomica(BigDecimal distancia, BigDecimal peso) {
    BigDecimal valorBase = new BigDecimal("10.00");
    BigDecimal taxaKm = new BigDecimal("0.75");
    BigDecimal taxaPeso = new BigDecimal("1.00");

    BigDecimal valorDistancia = distancia.multiply(taxaKm);
    BigDecimal valorPeso = peso.multiply(taxaPeso);

    return valorBase.add(valorDistancia).add(valorPeso).setScale(2, RoundingMode.HALF_UP);
  }

  // ==================== MÉTODOS UTILITÁRIOS ====================

  /**
   * Formata o valor como moeda brasileira.
   *
   * @return valor formatado (R$ 99,99)
   */
  public String getValorFormatado() {
    return String.format("R$ %.2f", valor);
  }

  /**
   * Retorna distância formatada.
   *
   * @return distância formatada (99,9 km)
   */
  public String getDistanciaFormatada() {
    return String.format("%.1f km", distanciaKm);
  }

  /**
   * Retorna prazo formatado.
   *
   * @return prazo formatado (5 dias úteis)
   */
  public String getPrazoFormatado() {
    return prazoDias == 1 ? "1 dia útil" : prazoDias + " dias úteis";
  }

  /**
   * Verifica se o frete foi calculado recentemente.
   *
   * @param minutosLimite Limite em minutos para considerar recente
   * @return true se foi calculado dentro do período
   */
  public boolean isCalculoRecente(int minutosLimite) {
    if (dataCalculo == null) return false;

    LocalDateTime limite = LocalDateTime.now().minusMinutes(minutosLimite);
    return dataCalculo.isAfter(limite);
  }

  // ==================== CALLBACKS JPA ====================

  /** Callback executado antes da persistência. Inicializa campos obrigatórios e validações. */
  @PrePersist
  public void onCreate() {
    if (dataCalculo == null) {
      dataCalculo = LocalDateTime.now();
    }

    // Validação de consistência
    if (tipoEntrega != null && prazoDias != null) {
      int prazoEsperado = tipoEntrega.getDiasMinimosEntrega();
      if (!prazoDias.equals(prazoEsperado)) {
        // Ajustar prazo conforme tipo se inconsistente
        this.prazoDias = prazoEsperado;
      }
    }

    // Normalizar observações
    if (observacoes != null) {
      observacoes = observacoes.trim();
      if (observacoes.isEmpty()) {
        observacoes = null;
      }
    }
  }

  /** Callback executado antes de atualizações. Valida alterações e mantém integridade. */
  @PreUpdate
  public void onUpdate() {
    // Atualizar timestamp se valor foi recalculado
    if (valor != null) {
      dataCalculo = LocalDateTime.now();
    }

    onCreate(); // Reutilizar validações
  }

  /**
   * Verifica se dois fretes são equivalentes (mesmo cálculo).
   *
   * @param outro Frete para comparar
   * @return true se são equivalentes
   */
  public boolean isEquivalente(Frete outro) {
    if (outro == null) return false;

    return tipoEntrega == outro.tipoEntrega
        && valor.compareTo(outro.valor) == 0
        && distanciaKm.compareTo(outro.distanciaKm) == 0
        && prazoDias.equals(outro.prazoDias);
  }
}
