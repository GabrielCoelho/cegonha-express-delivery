package br.com.cegonhaexpress.cegonha_express.model.entity;

import br.com.cegonhaexpress.cegonha_express.model.enums.StatusEncomenda;
import br.com.cegonhaexpress.cegonha_express.model.enums.TipoEntrega;
import br.com.cegonhaexpress.cegonha_express.model.enums.UF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FreteTest {

    private Cliente clienteTeste;
    private Endereco enderecoOrigemTeste;
    private Endereco enderecoDestinoTeste;
    private Encomenda encomendaTeste;

    @BeforeEach
    void setUp() {
        // Configuração de dados de teste
        clienteTeste = new Cliente("João Santos", "joao@teste.com", "987.654.321-00");

        enderecoOrigemTeste = new Endereco(
            "13840-000",
            "Rua das Palmeiras",
            "200",
            "Vila Nova",
            "Mogi Guaçu",
            UF.SP
        );

        enderecoDestinoTeste = new Endereco(
            "01310-100",
            "Av. Paulista",
            "1000",
            "Bela Vista",
            "São Paulo",
            UF.SP
        );

        encomendaTeste = new Encomenda(
            clienteTeste,
            enderecoOrigemTeste,
            enderecoDestinoTeste,
            TipoEntrega.PADRAO,
            "Bebê reborn teste para frete"
        );
    }

    @Nested
    @DisplayName("Testes de Construção de Objetos")
    class TestesConstrutores {

        @Test
        @DisplayName("Deve criar frete com construtor essencial")
        void deveCriarFreteComConstrutorEssencial() {
            System.out.println("\n=== TESTE: Criar Frete com Construtor Essencial ===");

            // Given
            BigDecimal valor = new BigDecimal("45.50");
            BigDecimal distancia = new BigDecimal("120.5");
            Integer prazo = 3;

            System.out.println("Dados de entrada:");
            System.out.println("- Tipo entrega: " + TipoEntrega.PADRAO);
            System.out.println("- Valor: R$ " + valor);
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Prazo: " + prazo + " dias");

            // When
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                valor,
                distancia,
                prazo
            );

            // Then
            System.out.println("\nResultados:");
            System.out.println("- Encomenda associada: " + (frete.getEncomenda() != null));
            System.out.println("- Tipo entrega: " + frete.getTipoEntrega());
            System.out.println("- Valor gravado: R$ " + frete.getValor());
            System.out.println("- Distância: " + frete.getDistanciaKm() + " km");
            System.out.println("- Prazo: " + frete.getPrazoDias() + " dias");
            System.out.println("- Data cálculo: " + frete.getDataCalculo());
            System.out.println("- CEP origem: " + frete.getCepOrigem());
            System.out.println("- CEP destino: " + frete.getCepDestino());

            assertNotNull(frete);
            assertEquals(encomendaTeste, frete.getEncomenda());
            assertEquals(TipoEntrega.PADRAO, frete.getTipoEntrega());
            assertEquals(valor, frete.getValor());
            assertEquals(distancia, frete.getDistanciaKm());
            assertEquals(prazo, frete.getPrazoDias());
            assertNotNull(frete.getDataCalculo());
            assertEquals(enderecoOrigemTeste.getCep(), frete.getCepOrigem());
            assertEquals(enderecoDestinoTeste.getCep(), frete.getCepDestino());

            System.out.println("✅ SUCESSO: Frete criado corretamente!");
        }

        @Test
        @DisplayName("Deve criar frete com construtor completo")
        void deveCriarFreteComConstrutorCompleto() {
            System.out.println("\n=== TESTE: Criar Frete com Construtor Completo ===");

            // Given
            BigDecimal valor = new BigDecimal("65.75");
            BigDecimal distancia = new BigDecimal("200.0");
            Integer prazo = 1;
            BigDecimal valorBase = new BigDecimal("25.00");
            BigDecimal taxaDistancia = new BigDecimal("1.50");
            BigDecimal taxaPeso = new BigDecimal("2.00");

            System.out.println("Dados de entrada:");
            System.out.println("- Tipo: " + TipoEntrega.EXPRESSA);
            System.out.println("- Valor final: R$ " + valor);
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Prazo: " + prazo + " dia");
            System.out.println("- Valor base: R$ " + valorBase);
            System.out.println("- Taxa/km: R$ " + taxaDistancia);
            System.out.println("- Taxa/kg: R$ " + taxaPeso);

            // When
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.EXPRESSA,
                valor,
                distancia,
                prazo,
                valorBase,
                taxaDistancia,
                taxaPeso
            );

            // Then
            System.out.println("\nResultados:");
            System.out.println("- Valor declarado na encomenda: R$ " + encomendaTeste.getValorDeclarado());
            System.out.println("- Taxa distância: R$ " + frete.getTaxaDistancia());
            System.out.println("- Taxa peso: R$ " + frete.getTaxaPeso());

            assertNotNull(frete);
            assertEquals(valorBase, encomendaTeste.getValorDeclarado());
            assertEquals(taxaDistancia, frete.getTaxaDistancia());
            assertEquals(taxaPeso, frete.getTaxaPeso());
            assertEquals(TipoEntrega.EXPRESSA, frete.getTipoEntrega());

            System.out.println("✅ SUCESSO: Frete completo criado corretamente!");
        }

        @Test
        @DisplayName("Deve definir data de cálculo automaticamente")
        void deveDefinirDataDeCalculoAutomaticamente() {
            System.out.println("\n=== TESTE: Data de Cálculo Automática ===");

            // Given
            LocalDateTime antes = LocalDateTime.now();
            System.out.println("Timestamp antes da criação: " + antes);

            // When
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.ECONOMICA,
                new BigDecimal("30.00"),
                new BigDecimal("100.0"),
                7
            );

            LocalDateTime depois = LocalDateTime.now();

            // Then
            System.out.println("Data cálculo do frete: " + frete.getDataCalculo());
            System.out.println("Timestamp após criação: " + depois);
            System.out.println("Data está entre os timestamps? " +
                (frete.getDataCalculo().isAfter(antes.minusSeconds(1)) &&
                 frete.getDataCalculo().isBefore(depois.plusSeconds(1))));

            assertNotNull(frete.getDataCalculo());
            assertTrue(frete.getDataCalculo().isAfter(antes.minusSeconds(1)));
            assertTrue(frete.getDataCalculo().isBefore(depois.plusSeconds(1)));

            System.out.println("✅ SUCESSO: Data de cálculo definida automaticamente!");
        }
    }

    @Nested
    @DisplayName("Testes de Cálculo de Fretes")
    class TestesCalculoFretes {

        @Test
        @DisplayName("Deve calcular frete expresso corretamente")
        void deveCalcularFreteExpressoCorretamente() {
            System.out.println("\n=== TESTE: Cálculo Frete Expresso ===");

            // Given
            BigDecimal distancia = new BigDecimal("100.0");
            BigDecimal peso = new BigDecimal("3.0");

            System.out.println("Parâmetros:");
            System.out.println("- Tipo: EXPRESSA");
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Peso: " + peso + " kg");
            System.out.println("\nFórmula EXPRESSA:");
            System.out.println("- Valor base: R$ 25,00");
            System.out.println("- Taxa/km: R$ 1,50");
            System.out.println("- Taxa/kg: R$ 2,00");

            // When
            BigDecimal valorCalculado = Frete.calcularFrete(TipoEntrega.EXPRESSA, distancia, peso);

            // Then
            // Cálculo esperado: 25.00 + (100.0 * 1.50) + (3.0 * 2.00) = 25.00 + 150.00 + 6.00 = 181.00
            BigDecimal valorEsperado = new BigDecimal("181.00");

            System.out.println("\nCálculo detalhado:");
            System.out.println("- Base: R$ 25,00");
            System.out.println("- Distância: " + distancia + " × R$ 1,50 = R$ " + distancia.multiply(new BigDecimal("1.50")));
            System.out.println("- Peso: " + peso + " × R$ 2,00 = R$ " + peso.multiply(new BigDecimal("2.00")));
            System.out.println("- Total calculado: R$ " + valorCalculado);
            System.out.println("- Total esperado: R$ " + valorEsperado);

            assertEquals(valorEsperado, valorCalculado);
            System.out.println("✅ SUCESSO: Frete expresso calculado corretamente!");
        }

        @Test
        @DisplayName("Deve calcular frete padrão corretamente")
        void deveCalcularFretePadraoCorretamente() {
            System.out.println("\n=== TESTE: Cálculo Frete Padrão ===");

            // Given
            BigDecimal distancia = new BigDecimal("80.0");
            BigDecimal peso = new BigDecimal("2.5");

            System.out.println("Parâmetros:");
            System.out.println("- Tipo: PADRÃO");
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Peso: " + peso + " kg");
            System.out.println("\nFórmula PADRÃO:");
            System.out.println("- Valor base: R$ 15,00");
            System.out.println("- Taxa/km: R$ 1,00");
            System.out.println("- Taxa/kg: R$ 1,50");

            // When
            BigDecimal valorCalculado = Frete.calcularFrete(TipoEntrega.PADRAO, distancia, peso);

            // Then
            // Cálculo esperado: 15.00 + (80.0 * 1.00) + (2.5 * 1.50) = 15.00 + 80.00 + 3.75 = 98.75
            BigDecimal valorEsperado = new BigDecimal("98.75");

            System.out.println("\nCálculo detalhado:");
            System.out.println("- Base: R$ 15,00");
            System.out.println("- Distância: " + distancia + " × R$ 1,00 = R$ " + distancia.multiply(BigDecimal.ONE));
            System.out.println("- Peso: " + peso + " × R$ 1,50 = R$ " + peso.multiply(new BigDecimal("1.50")));
            System.out.println("- Total calculado: R$ " + valorCalculado);
            System.out.println("- Total esperado: R$ " + valorEsperado);

            assertEquals(valorEsperado, valorCalculado);
            System.out.println("✅ SUCESSO: Frete padrão calculado corretamente!");
        }

        @Test
        @DisplayName("Deve calcular frete econômico corretamente")
        void deveCalcularFreteEconomicoCorretamente() {
            System.out.println("\n=== TESTE: Cálculo Frete Econômico ===");

            // Given
            BigDecimal distancia = new BigDecimal("150.0");
            BigDecimal peso = new BigDecimal("4.0");

            System.out.println("Parâmetros:");
            System.out.println("- Tipo: ECONÔMICA");
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Peso: " + peso + " kg");
            System.out.println("\nFórmula ECONÔMICA:");
            System.out.println("- Valor base: R$ 10,00");
            System.out.println("- Taxa/km: R$ 0,75");
            System.out.println("- Taxa/kg: R$ 1,00");

            // When
            BigDecimal valorCalculado = Frete.calcularFrete(TipoEntrega.ECONOMICA, distancia, peso);

            // Then
            // Cálculo esperado: 10.00 + (150.0 * 0.75) + (4.0 * 1.00) = 10.00 + 112.50 + 4.00 = 126.50
            BigDecimal valorEsperado = new BigDecimal("126.50");

            System.out.println("\nCálculo detalhado:");
            System.out.println("- Base: R$ 10,00");
            System.out.println("- Distância: " + distancia + " × R$ 0,75 = R$ " + distancia.multiply(new BigDecimal("0.75")));
            System.out.println("- Peso: " + peso + " × R$ 1,00 = R$ " + peso.multiply(BigDecimal.ONE));
            System.out.println("- Total calculado: R$ " + valorCalculado);
            System.out.println("- Total esperado: R$ " + valorEsperado);

            assertEquals(valorEsperado, valorCalculado);
            System.out.println("✅ SUCESSO: Frete econômico calculado corretamente!");
        }

        @Test
        @DisplayName("Deve usar peso padrão quando peso for null")
        void deveUsarPesoPadraoQuandoPesoForNull() {
            System.out.println("\n=== TESTE: Peso Padrão quando Null ===");

            // Given
            BigDecimal distancia = new BigDecimal("50.0");
            BigDecimal peso = null;

            System.out.println("Parâmetros:");
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Peso: " + peso + " (null)");
            System.out.println("- Peso padrão usado: 1,0 kg");

            // When
            BigDecimal valorCalculado = Frete.calcularFrete(TipoEntrega.PADRAO, distancia, peso);

            // Then
            // Cálculo com peso = 1.0: 15.00 + (50.0 * 1.00) + (1.0 * 1.50) = 15.00 + 50.00 + 1.50 = 66.50
            BigDecimal valorEsperado = new BigDecimal("66.50");

            System.out.println("\nCálculo com peso padrão:");
            System.out.println("- Base: R$ 15,00");
            System.out.println("- Distância: R$ " + distancia.multiply(BigDecimal.ONE));
            System.out.println("- Peso (1kg): R$ " + BigDecimal.ONE.multiply(new BigDecimal("1.50")));
            System.out.println("- Total: R$ " + valorCalculado);

            assertEquals(valorEsperado, valorCalculado);
            System.out.println("✅ SUCESSO: Peso padrão aplicado corretamente!");
        }

        @Test
        @DisplayName("Deve lançar exceção para parâmetros inválidos")
        void deveLancarExcecaoParaParametrosInvalidos() {
            System.out.println("\n=== TESTE: Exceção para Parâmetros Inválidos ===");

            // Test 1: Tipo null
            System.out.println("Teste 1: Tipo null");
            IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> Frete.calcularFrete(null, new BigDecimal("100.0"), new BigDecimal("2.0"))
            );
            System.out.println("Exceção 1: " + exception1.getMessage());

            // Test 2: Distância null
            System.out.println("\nTeste 2: Distância null");
            IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> Frete.calcularFrete(TipoEntrega.PADRAO, null, new BigDecimal("2.0"))
            );
            System.out.println("Exceção 2: " + exception2.getMessage());

            assertEquals("Tipo de entrega e distância são obrigatórios", exception1.getMessage());
            assertEquals("Tipo de entrega e distância são obrigatórios", exception2.getMessage());
            System.out.println("✅ SUCESSO: Exceções lançadas corretamente!");
        }
    }

    @Nested
    @DisplayName("Testes de Métodos de Negócio")
    class TestesMetodosNegocio {

        private Frete freteTeste;

        @BeforeEach
        void setUp() {
            freteTeste = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                new BigDecimal("50.00"),
                new BigDecimal("75.0"),
                3
            );
        }

        @Test
        @DisplayName("Deve recalcular frete com novos parâmetros")
        void deveRecalcularFreteComNovosParametros() {
            System.out.println("\n=== TESTE: Recalcular Frete ===");

            // Given
            BigDecimal novaDistancia = new BigDecimal("120.0");
            BigDecimal novoPeso = new BigDecimal("3.5");
            BigDecimal valorAnterior = freteTeste.getValor();
            LocalDateTime dataAnterior = freteTeste.getDataCalculo();

            System.out.println("Valores iniciais:");
            System.out.println("- Valor anterior: R$ " + valorAnterior);
            System.out.println("- Distância anterior: " + freteTeste.getDistanciaKm() + " km");
            System.out.println("- Data anterior: " + dataAnterior);

            System.out.println("\nNovos parâmetros:");
            System.out.println("- Nova distância: " + novaDistancia + " km");
            System.out.println("- Novo peso: " + novoPeso + " kg");

            // When
            freteTeste.recalcular(novaDistancia, novoPeso);

            // Then
            BigDecimal novoValor = freteTeste.getValor();
            // Cálculo esperado: 15.00 + (120.0 * 1.00) + (3.5 * 1.50) = 15.00 + 120.00 + 5.25 = 140.25
            BigDecimal valorEsperado = new BigDecimal("140.25");

            System.out.println("\nApós recálculo:");
            System.out.println("- Novo valor: R$ " + novoValor);
            System.out.println("- Nova distância: " + freteTeste.getDistanciaKm() + " km");
            System.out.println("- Peso na encomenda: " + encomendaTeste.getPesoKg() + " kg");
            System.out.println("- Nova data: " + freteTeste.getDataCalculo());
            System.out.println("- Data foi atualizada? " + freteTeste.getDataCalculo().isAfter(dataAnterior));

            assertEquals(valorEsperado, novoValor);
            assertEquals(novaDistancia, freteTeste.getDistanciaKm());
            assertEquals(novoPeso, encomendaTeste.getPesoKg());
            assertTrue(freteTeste.getDataCalculo().isAfter(dataAnterior));

            System.out.println("✅ SUCESSO: Frete recalculado corretamente!");
        }

        @Test
        @DisplayName("Deve verificar se está dentro do prazo")
        void deveVerificarSeEstaDentroDoPrazo() {
            System.out.println("\n=== TESTE: Verificar Prazo ===");

            // Given - encomenda sem data estimada
            System.out.println("Cenário 1: Encomenda sem data estimada");
            System.out.println("- Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            System.out.println("- Dentro do prazo? " + freteTeste.isDentroDoPrazo());
            assertTrue(freteTeste.isDentroDoPrazo()); // true quando não há data para comparar

            // When - definir data futura
            System.out.println("\nCenário 2: Data estimada no futuro");
            encomendaTeste.setDataEstimadaEntrega(LocalDateTime.now().toLocalDate().plusDays(5));
            System.out.println("- Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            System.out.println("- Prazo do frete: " + freteTeste.getPrazoDias() + " dias");
            System.out.println("- Dentro do prazo? " + freteTeste.isDentroDoPrazo());
            assertTrue(freteTeste.isDentroDoPrazo());

            // When - definir data no passado
            System.out.println("\nCenário 3: Data estimada no passado");
            encomendaTeste.setDataEstimadaEntrega(LocalDateTime.now().toLocalDate().minusDays(1));
            System.out.println("- Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            System.out.println("- Dentro do prazo? " + freteTeste.isDentroDoPrazo());
            assertFalse(freteTeste.isDentroDoPrazo());

            System.out.println("✅ SUCESSO: Verificação de prazo funcionando!");
        }

        @Test
        @DisplayName("Deve verificar se é interestadual")
        void deveVerificarSeEInterestadual() {
            System.out.println("\n=== TESTE: Verificar Frete Interestadual ===");

            // Given - mesmo estado (SP)
            System.out.println("Cenário 1: Mesmo estado");
            System.out.println("- Origem: " + enderecoOrigemTeste.getUf());
            System.out.println("- Destino: " + enderecoDestinoTeste.getUf());
            System.out.println("- É interestadual? " + freteTeste.isInterestadual());
            assertFalse(freteTeste.isInterestadual());

            // When - alterar destino para outro estado
            System.out.println("\nCenário 2: Estados diferentes");
            Endereco enderecoRJ = new Endereco(
                "22071-900", "Copacabana", "500", "Copacabana", "Rio de Janeiro", UF.RJ
            );
            encomendaTeste.setEnderecoDestino(enderecoRJ);
            System.out.println("- Origem: " + encomendaTeste.getEnderecoOrigem().getUf());
            System.out.println("- Destino: " + encomendaTeste.getEnderecoDestino().getUf());
            System.out.println("- É interestadual? " + freteTeste.isInterestadual());
            assertTrue(freteTeste.isInterestadual());

            System.out.println("✅ SUCESSO: Verificação interestadual funcionando!");
        }

        @Test
        @DisplayName("Deve calcular valor por quilômetro")
        void deveCalcularValorPorQuilometro() {
            System.out.println("\n=== TESTE: Calcular Valor por Quilômetro ===");

            // Given
            System.out.println("Dados do frete:");
            System.out.println("- Valor total: R$ " + freteTeste.getValor());
            System.out.println("- Distância: " + freteTeste.getDistanciaKm() + " km");

            // When
            BigDecimal valorPorKm = freteTeste.getValorPorKm();

            // Then
            // 50.00 / 75.0 = 0.6667 (com 4 casas decimais)
            BigDecimal valorEsperado = new BigDecimal("50.00")
                .divide(new BigDecimal("75.0"), 4, RoundingMode.HALF_UP);

            System.out.println("- Valor por km calculado: R$ " + valorPorKm);
            System.out.println("- Valor por km esperado: R$ " + valorEsperado);

            assertEquals(valorEsperado, valorPorKm);
            System.out.println("✅ SUCESSO: Valor por km calculado corretamente!");
        }

        @Test
        @DisplayName("Deve retornar zero para valor por km com distância zero")
        void deveRetornarZeroParaValorPorKmComDistanciaZero() {
            System.out.println("\n=== TESTE: Valor por Km com Distância Zero ===");

            // Given
            freteTeste.setDistanciaKm(BigDecimal.ZERO);
            System.out.println("Distância alterada para: " + freteTeste.getDistanciaKm());

            // When
            BigDecimal valorPorKm = freteTeste.getValorPorKm();

            // Then
            System.out.println("Valor por km retornado: R$ " + valorPorKm);
            assertEquals(BigDecimal.ZERO, valorPorKm);
            System.out.println("✅ SUCESSO: Retorna zero para distância zero!");
        }

        @Test
        @DisplayName("Deve verificar se são fretes equivalentes")
        void deveVerificarSeSaoFretesEquivalentes() {
            System.out.println("\n=== TESTE: Verificar Fretes Equivalentes ===");

            // Given - frete idêntico
            Frete freteIdentico = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                new BigDecimal("50.00"),
                new BigDecimal("75.0"),
                3
            );

            System.out.println("Frete 1:");
            System.out.println("- Tipo: " + freteTeste.getTipoEntrega());
            System.out.println("- Valor: R$ " + freteTeste.getValor());
            System.out.println("- Distância: " + freteTeste.getDistanciaKm() + " km");
            System.out.println("- Prazo: " + freteTeste.getPrazoDias() + " dias");

            System.out.println("\nFrete 2:");
            System.out.println("- Tipo: " + freteIdentico.getTipoEntrega());
            System.out.println("- Valor: R$ " + freteIdentico.getValor());
            System.out.println("- Distância: " + freteIdentico.getDistanciaKm() + " km");
            System.out.println("- Prazo: " + freteIdentico.getPrazoDias() + " dias");

            // When & Then
            System.out.println("\nSão equivalentes? " + freteTeste.isEquivalente(freteIdentico));
            assertTrue(freteTeste.isEquivalente(freteIdentico));

            // Given - frete diferente
            Frete freteDiferente = new Frete(
                encomendaTeste,
                TipoEntrega.EXPRESSA,
                new BigDecimal("75.00"),
                new BigDecimal("75.0"),
                1
            );

            System.out.println("\nFrete diferente:");
            System.out.println("- Tipo: " + freteDiferente.getTipoEntrega());
            System.out.println("- Valor: R$ " + freteDiferente.getValor());
            System.out.println("- Distância: " + freteDiferente.getDistanciaKm() + " km");
            System.out.println("- Prazo: " + freteDiferente.getPrazoDias() + " dias");
            System.out.println("São equivalentes? " + freteTeste.isEquivalente(freteDiferente));

            assertFalse(freteTeste.isEquivalente(freteDiferente));
            assertFalse(freteTeste.isEquivalente(null));

            System.out.println("✅ SUCESSO: Verificação de equivalência funcionando!");
        }
    }

    @Nested
    @DisplayName("Testes de Métodos Utilitários")
    class TestesMetodosUtilitarios {

        private Frete freteTeste;

        @BeforeEach
        void setUp() {
            freteTeste = new Frete(
                encomendaTeste,
                TipoEntrega.EXPRESSA,
                new BigDecimal("85.50"),
                new BigDecimal("125.5"),
                1
            );
        }

        @Test
        @DisplayName("Deve formatar valor como moeda")
        void deveFormatarValorComoMoeda() {
            System.out.println("\n=== TESTE: Formatar Valor como Moeda ===");

            // Given
            System.out.println("Valor original: " + freteTeste.getValor());

            // When
            String valorFormatado = freteTeste.getValorFormatado();

            // Then
            System.out.println("Valor formatado: " + valorFormatado);
            assertEquals("R$ 85,50", valorFormatado);
            System.out.println("✅ SUCESSO: Valor formatado corretamente!");
        }

        @Test
        @DisplayName("Deve formatar distância")
        void deveFormatarDistancia() {
            System.out.println("\n=== TESTE: Formatar Distância ===");

            // Given
            System.out.println("Distância original: " + freteTeste.getDistanciaKm());

            // When
            String distanciaFormatada = freteTeste.getDistanciaFormatada();

            // Then
            System.out.println("Distância formatada: " + distanciaFormatada);
            assertEquals("125,5 km", distanciaFormatada);
            System.out.println("✅ SUCESSO: Distância formatada corretamente!");
        }

        @Test
        @DisplayName("Deve formatar prazo singular e plural")
        void deveFormatarPrazoSingularEPlural() {
            System.out.println("\n=== TESTE: Formatar Prazo Singular e Plural ===");

            // Test prazo singular (1 dia)
            System.out.println("Cenário 1: Prazo de 1 dia");
            System.out.println("- Prazo: " + freteTeste.getPrazoDias());
            String prazo1 = freteTeste.getPrazoFormatado();
            System.out.println("- Formatado: " + prazo1);
            assertEquals("1 dia útil", prazo1);

            // Test prazo plural (3 dias)
            System.out.println("\nCenário 2: Prazo de 3 dias");
            freteTeste.setPrazoDias(3);
            System.out.println("- Prazo: " + freteTeste.getPrazoDias());
            String prazo3 = freteTeste.getPrazoFormatado();
            System.out.println("- Formatado: " + prazo3);
            assertEquals("3 dias úteis", prazo3);

            System.out.println("✅ SUCESSO: Prazo formatado corretamente!");
        }

        @Test
        @DisplayName("Deve gerar resumo do frete")
        void deveGerarResumoDoFrete() {
            System.out.println("\n=== TESTE: Gerar Resumo do Frete ===");

            // When
            String resumo = freteTeste.getResumo();

            // Then
            System.out.println("Resumo gerado: " + resumo);
            System.out.println("Contém tipo? " + resumo.contains("Expressa"));
            System.out.println("Contém valor? " + resumo.contains("85,50"));
            System.out.println("Contém prazo? " + resumo.contains("1 dias"));
            System.out.println("Contém distância? " + resumo.contains("125,5"));

            assertTrue(resumo.contains("Expressa"));
            assertTrue(resumo.contains("85,50"));
            assertTrue(resumo.contains("1 dias"));
            assertTrue(resumo.contains("125,5"));

            System.out.println("✅ SUCESSO: Resumo gerado corretamente!");
        }

        @Test
        @DisplayName("Deve verificar se cálculo é recente")
        void deveVerificarSeCalculoERecente() {
            System.out.println("\n=== TESTE: Verificar Cálculo Recente ===");

            // Given - frete acabou de ser criado
            System.out.println("Data do cálculo: " + freteTeste.getDataCalculo());
            System.out.println("Data atual: " + LocalDateTime.now());

            // Test recente (5 minutos)
            System.out.println("\nTeste 1: Limite de 5 minutos");
            boolean recente5min = freteTeste.isCalculoRecente(5);
            System.out.println("- É recente (5 min)? " + recente5min);
            assertTrue(recente5min);

            // Test não recente (simular data antiga)
            System.out.println("\nTeste 2: Simulando data antiga");
            freteTeste.setDataCalculo(LocalDateTime.now().minusMinutes(10));
            System.out.println("- Data alterada para: " + freteTeste.getDataCalculo());
            boolean recente1min = freteTeste.isCalculoRecente(1);
            System.out.println("- É recente (1 min)? " + recente1min);
            assertFalse(recente1min);

            System.out.println("✅ SUCESSO: Verificação de cálculo recente funcionando!");
        }

        @Test
        @DisplayName("Deve retornar false para data de cálculo null")
        void deveRetornarFalseParaDataDeCalculoNull() {
            System.out.println("\n=== TESTE: Data de Cálculo Null ===");

            // Given
            freteTeste.setDataCalculo(null);
            System.out.println("Data de cálculo: " + freteTeste.getDataCalculo());

            // When
            boolean recente = freteTeste.isCalculoRecente(5);

            // Then
            System.out.println("É recente? " + recente);
            assertFalse(recente);
            System.out.println("✅ SUCESSO: Retorna false para data null!");
        }
    }

    @Nested
    @DisplayName("Testes de Callbacks JPA")
    class TestesCallbacksJPA {

        @Test
        @DisplayName("Deve inicializar campos no @PrePersist")
        void deveInicializarCamposNoPrePersist() {
            System.out.println("\n=== TESTE: Callback @PrePersist ===");

            // Given
            Frete frete = new Frete();
            frete.setEncomenda(encomendaTeste);
            frete.setTipoEntrega(TipoEntrega.PADRAO);
            frete.setValor(new BigDecimal("40.00"));
            frete.setDistanciaKm(new BigDecimal("60.0"));
            frete.setPrazoDias(3);

            System.out.println("Antes do @PrePersist:");
            System.out.println("- Data cálculo: " + frete.getDataCalculo());
            System.out.println("- Tipo entrega: " + frete.getTipoEntrega());
            System.out.println("- Prazo: " + frete.getPrazoDias());

            // When
            frete.onCreate(); // Simula @PrePersist

            // Then
            System.out.println("\nApós @PrePersist:");
            System.out.println("- Data cálculo: " + frete.getDataCalculo());
            System.out.println("- Prazo ajustado: " + frete.getPrazoDias());

            assertNotNull(frete.getDataCalculo());
            assertEquals(Integer.valueOf(3), frete.getPrazoDias()); // PADRAO = 3 dias

            System.out.println("✅ SUCESSO: Campos inicializados pelo @PrePersist!");
        }

        @Test
        @DisplayName("Deve ajustar prazo inconsistente no @PrePersist")
        void deveAjustarPrazoInconsistenteNoPrePersist() {
            System.out.println("\n=== TESTE: Ajustar Prazo Inconsistente ===");

            // Given - prazo inconsistente com tipo
            Frete frete = new Frete();
            frete.setEncomenda(encomendaTeste);
            frete.setTipoEntrega(TipoEntrega.EXPRESSA); // Deveria ser 1 dia
            frete.setPrazoDias(5); // Prazo errado
            frete.setValor(new BigDecimal("60.00"));
            frete.setDistanciaKm(new BigDecimal("80.0"));

            System.out.println("Antes da correção:");
            System.out.println("- Tipo: " + frete.getTipoEntrega() + " (esperado: 1 dia)");
            System.out.println("- Prazo informado: " + frete.getPrazoDias() + " dias");

            // When
            frete.onCreate();

            // Then
            System.out.println("\nApós correção:");
            System.out.println("- Prazo corrigido: " + frete.getPrazoDias() + " dias");

            assertEquals(Integer.valueOf(1), frete.getPrazoDias()); // Corrigido para EXPRESSA
            System.out.println("✅ SUCESSO: Prazo ajustado automaticamente!");
        }

        @Test
        @DisplayName("Deve normalizar observações no @PrePersist e @PreUpdate")
        void deveNormalizarObservacoesNoPrePersistEPreUpdate() {
            System.out.println("\n=== TESTE: Normalizar Observações ===");

            // Given
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.ECONOMICA,
                new BigDecimal("35.00"),
                new BigDecimal("90.0"),
                7
            );

            // Test @PrePersist
            String observacaoComEspacos = "   Observação com espaços   ";
            frete.setObservacoes(observacaoComEspacos);
            System.out.println("Observação antes @PrePersist: '" + observacaoComEspacos + "'");

            frete.onCreate();
            System.out.println("Observação após @PrePersist: '" + frete.getObservacoes() + "'");
            assertEquals("Observação com espaços", frete.getObservacoes());

            // Test @PreUpdate com string vazia
            System.out.println("\nTeste com observação vazia:");
            frete.setObservacoes("   ");
            System.out.println("Observação antes @PreUpdate: '" + frete.getObservacoes() + "'");

            frete.onUpdate();
            System.out.println("Observação após @PreUpdate: " + frete.getObservacoes());
            assertNull(frete.getObservacoes());

            System.out.println("✅ SUCESSO: Observações normalizadas corretamente!");
        }

        @Test
        @DisplayName("Deve atualizar data no @PreUpdate")
        void deveAtualizarDataNoPreUpdate() {
            System.out.println("\n=== TESTE: Atualizar Data no @PreUpdate ===");

            // Given
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                new BigDecimal("45.00"),
                new BigDecimal("70.0"),
                3
            );

            LocalDateTime dataOriginal = frete.getDataCalculo();
            System.out.println("Data original: " + dataOriginal);

            // Aguardar um pouco para garantir diferença temporal
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // When
            frete.onUpdate();

            // Then
            LocalDateTime dataNova = frete.getDataCalculo();
            System.out.println("Data após @PreUpdate: " + dataNova);
            System.out.println("Data foi atualizada? " + dataNova.isAfter(dataOriginal));

            assertTrue(dataNova.isAfter(dataOriginal));
            System.out.println("✅ SUCESSO: Data atualizada no @PreUpdate!");
        }
    }

    @Nested
    @DisplayName("Testes de Validação")
    class TestesValidacao {

        @Test
        @DisplayName("Deve aceitar frete com dados válidos")
        void deveAceitarFreteComDadosValidos() {
            System.out.println("\n=== TESTE: Validação com Dados Válidos ===");

            // Given
            BigDecimal valor = new BigDecimal("75.25");
            BigDecimal distancia = new BigDecimal("150.5");
            Integer prazo = 3;
            BigDecimal taxaDistancia = new BigDecimal("1.25");
            BigDecimal taxaPeso = new BigDecimal("1.75");
            String observacoes = "Frete com observações importantes";

            System.out.println("Dados de entrada:");
            System.out.println("- Valor: R$ " + valor);
            System.out.println("- Distância: " + distancia + " km");
            System.out.println("- Prazo: " + prazo + " dias");
            System.out.println("- Taxa distância: R$ " + taxaDistancia);
            System.out.println("- Taxa peso: R$ " + taxaPeso);
            System.out.println("- Observações: " + observacoes);

            // When
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                valor,
                distancia,
                prazo,
                new BigDecimal("15.00"),
                taxaDistancia,
                taxaPeso
            );
            frete.setObservacoes(observacoes);

            // Then
            System.out.println("\nDados gravados:");
            System.out.println("- Valor: R$ " + frete.getValor());
            System.out.println("- Distância: " + frete.getDistanciaKm() + " km");
            System.out.println("- Prazo: " + frete.getPrazoDias() + " dias");
            System.out.println("- Taxa distância: R$ " + frete.getTaxaDistancia());
            System.out.println("- Taxa peso: R$ " + frete.getTaxaPeso());
            System.out.println("- Observações: " + frete.getObservacoes());

            assertNotNull(frete);
            assertEquals(valor, frete.getValor());
            assertEquals(distancia, frete.getDistanciaKm());
            assertEquals(prazo, frete.getPrazoDias());
            assertEquals(taxaDistancia, frete.getTaxaDistancia());
            assertEquals(taxaPeso, frete.getTaxaPeso());
            assertEquals(observacoes, frete.getObservacoes());

            System.out.println("✅ SUCESSO: Frete aceito com dados válidos!");
        }

        @Test
        @DisplayName("Deve aceitar frete sem campos opcionais")
        void deveAceitarFreteSemCamposOpcionais() {
            System.out.println("\n=== TESTE: Validação sem Campos Opcionais ===");

            // Given & When
            System.out.println("Criando frete apenas com campos obrigatórios:");
            System.out.println("- Campos opcionais: não informados");

            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.EXPRESSA,
                new BigDecimal("65.00"),
                new BigDecimal("100.0"),
                1
            );

            // Then
            System.out.println("\nCampos opcionais verificados:");
            System.out.println("- Taxa distância: " + frete.getTaxaDistancia());
            System.out.println("- Taxa peso: " + frete.getTaxaPeso());
            System.out.println("- Observações: " + frete.getObservacoes());

            assertNotNull(frete);
            assertNull(frete.getTaxaDistancia());
            assertNull(frete.getTaxaPeso());
            assertNull(frete.getObservacoes());

            // Mas campos obrigatórios devem estar preenchidos
            assertNotNull(frete.getEncomenda());
            assertNotNull(frete.getTipoEntrega());
            assertNotNull(frete.getValor());
            assertNotNull(frete.getDistanciaKm());
            assertNotNull(frete.getPrazoDias());

            System.out.println("✅ SUCESSO: Frete criado sem campos opcionais!");
        }

        @Test
        @DisplayName("Deve extrair CEPs da encomenda automaticamente")
        void deveExtrairCEPsDaEncomendaAutomaticamente() {
            System.out.println("\n=== TESTE: Extração Automática de CEPs ===");

            // Given
            System.out.println("CEPs dos endereços:");
            System.out.println("- Origem: " + enderecoOrigemTeste.getCep());
            System.out.println("- Destino: " + enderecoDestinoTeste.getCep());

            // When
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                new BigDecimal("40.00"),
                new BigDecimal("80.0"),
                3
            );

            // Then
            System.out.println("\nCEPs extraídos para o frete:");
            System.out.println("- CEP origem: " + frete.getCepOrigem());
            System.out.println("- CEP destino: " + frete.getCepDestino());

            assertEquals(enderecoOrigemTeste.getCep(), frete.getCepOrigem());
            assertEquals(enderecoDestinoTeste.getCep(), frete.getCepDestino());

            System.out.println("✅ SUCESSO: CEPs extraídos automaticamente!");
        }

        @Test
        @DisplayName("Deve lidar com encomenda sem endereços")
        void deveLidarComEncomendaSemEnderecos() {
            System.out.println("\n=== TESTE: Encomenda sem Endereços ===");

            // Given - encomenda sem endereços definidos
            Encomenda encomendaSemEnderecos = new Encomenda();
            encomendaSemEnderecos.setCliente(clienteTeste);
            encomendaSemEnderecos.setTipoEntrega(TipoEntrega.PADRAO);
            encomendaSemEnderecos.setDescricaoBebe("Teste sem endereços");

            System.out.println("Encomenda sem endereços:");
            System.out.println("- Endereço origem: " + encomendaSemEnderecos.getEnderecoOrigem());
            System.out.println("- Endereço destino: " + encomendaSemEnderecos.getEnderecoDestino());

            // When
            Frete frete = new Frete(
                encomendaSemEnderecos,
                TipoEntrega.PADRAO,
                new BigDecimal("30.00"),
                new BigDecimal("50.0"),
                3
            );

            // Then
            System.out.println("\nCEPs do frete:");
            System.out.println("- CEP origem: " + frete.getCepOrigem());
            System.out.println("- CEP destino: " + frete.getCepDestino());

            assertNull(frete.getCepOrigem());
            assertNull(frete.getCepDestino());

            System.out.println("✅ SUCESSO: Frete criado mesmo sem endereços!");
        }
    }
}
