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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EncomendaTest {

    private Cliente clienteTeste;
    private Endereco enderecoOrigemTeste;
    private Endereco enderecoDestinoTeste;

    @BeforeEach
    void setUp() {
        // Configuração de dados de teste
        clienteTeste = new Cliente("Maria Silva", "maria@teste.com", "123.456.789-00");

        enderecoOrigemTeste = new Endereco(
            "13840-000",
            "Rua das Flores",
            "123",
            "Centro",
            "Mogi Guaçu",
            UF.SP
        );

        enderecoDestinoTeste = new Endereco(
            "01001-000",
            "Praça da Sé",
            "456",
            "Centro",
            "São Paulo",
            UF.SP
        );
    }

    @Nested
    @DisplayName("Testes de Construção de Objetos")
    class TestesConstrutores {

        @Test
        @DisplayName("Deve criar encomenda com construtor essencial")
        void deveCriarEncomendaComConstrutorEssencial() {
            System.out.println("\n=== TESTE: Criar Encomenda com Construtor Essencial ===");

            // Given
            String descricaoBebe = "Bebê reborn Alice, 50cm, cabelo loiro";
            System.out.println("Dados de entrada:");
            System.out.println("- Cliente: " + clienteTeste.getNome());
            System.out.println("- Descrição: " + descricaoBebe);
            System.out.println("- Tipo Entrega: " + TipoEntrega.PADRAO);

            // When
            Encomenda encomenda = new Encomenda(
                clienteTeste,
                enderecoOrigemTeste,
                enderecoDestinoTeste,
                TipoEntrega.PADRAO,
                descricaoBebe
            );

            // Then
            System.out.println("\nResultados:");
            System.out.println("- Código gerado: " + encomenda.getCodigo());
            System.out.println("- Status inicial: " + encomenda.getStatus());
            System.out.println("- Data pedido: " + encomenda.getDataPedido());

            assertNotNull(encomenda);
            assertEquals(clienteTeste, encomenda.getCliente());
            assertEquals(enderecoOrigemTeste, encomenda.getEnderecoOrigem());
            assertEquals(enderecoDestinoTeste, encomenda.getEnderecoDestino());
            assertEquals(TipoEntrega.PADRAO, encomenda.getTipoEntrega());
            assertEquals(descricaoBebe, encomenda.getDescricaoBebe());
            assertEquals(StatusEncomenda.PENDENTE, encomenda.getStatus());
            assertNotNull(encomenda.getDataPedido());
            assertNotNull(encomenda.getCodigo());
            assertTrue(encomenda.getCodigo().startsWith("CE"));

            System.out.println("✅ SUCESSO: Encomenda criada corretamente!");
        }

        @Test
        @DisplayName("Deve criar encomenda com construtor completo")
        void deveCriarEncomendaComConstrutorCompleto() {
            System.out.println("\n=== TESTE: Criar Encomenda com Construtor Completo ===");

            // Given
            String descricaoBebe = "Bebê reborn Pedro, 45cm, cabelo castanho";
            BigDecimal peso = new BigDecimal("2.5");
            BigDecimal altura = new BigDecimal("45.0");
            BigDecimal valorDeclarado = new BigDecimal("300.00");

            System.out.println("Dados de entrada:");
            System.out.println("- Descrição: " + descricaoBebe);
            System.out.println("- Peso: " + peso + " kg");
            System.out.println("- Altura: " + altura + " cm");
            System.out.println("- Valor declarado: R$ " + valorDeclarado);

            // When
            Encomenda encomenda = new Encomenda(
                clienteTeste,
                enderecoOrigemTeste,
                enderecoDestinoTeste,
                TipoEntrega.EXPRESSA,
                descricaoBebe,
                peso,
                altura,
                valorDeclarado
            );

            // Then
            System.out.println("\nResultados:");
            System.out.println("- Peso gravado: " + encomenda.getPesoKg() + " kg");
            System.out.println("- Altura gravada: " + encomenda.getAlturaCm() + " cm");
            System.out.println("- Valor declarado: R$ " + encomenda.getValorDeclarado());
            System.out.println("- Status: " + encomenda.getStatus());

            assertNotNull(encomenda);
            assertEquals(peso, encomenda.getPesoKg());
            assertEquals(altura, encomenda.getAlturaCm());
            assertEquals(valorDeclarado, encomenda.getValorDeclarado());
            assertEquals(StatusEncomenda.PENDENTE, encomenda.getStatus());

            System.out.println("✅ SUCESSO: Encomenda completa criada corretamente!");
        }

        @Test
        @DisplayName("Deve gerar código único para cada encomenda")
        void deveGerarCodigoUnicoParaCadaEncomenda() {
            System.out.println("\n=== TESTE: Gerar Código Único para Encomendas ===");

            // Given & When
            Encomenda encomenda1 = new Encomenda(
                clienteTeste, enderecoOrigemTeste, enderecoDestinoTeste,
                TipoEntrega.PADRAO, "Bebê 1"
            );

            Encomenda encomenda2 = new Encomenda(
                clienteTeste, enderecoOrigemTeste, enderecoDestinoTeste,
                TipoEntrega.PADRAO, "Bebê 2"
            );

            // Then
            System.out.println("Códigos gerados:");
            System.out.println("- Encomenda 1: " + encomenda1.getCodigo());
            System.out.println("- Encomenda 2: " + encomenda2.getCodigo());
            System.out.println("- São diferentes? " + !encomenda1.getCodigo().equals(encomenda2.getCodigo()));

            assertNotEquals(encomenda1.getCodigo(), encomenda2.getCodigo());
            assertTrue(encomenda1.getCodigo().startsWith("CE"));
            assertTrue(encomenda2.getCodigo().startsWith("CE"));

            System.out.println("✅ SUCESSO: Códigos únicos gerados corretamente!");
        }
    }

    @Nested
    @DisplayName("Testes de Fluxo de Status")
    class TestesFluxoStatus {

        private Encomenda encomendaTeste;

        @BeforeEach
        void setUp() {
            encomendaTeste = new Encomenda(
                clienteTeste,
                enderecoOrigemTeste,
                enderecoDestinoTeste,
                TipoEntrega.PADRAO,
                "Bebê teste"
            );
        }

        @Test
        @DisplayName("Deve confirmar encomenda pendente")
        void deveConfirmarEncomendaPendente() {
            System.out.println("\n=== TESTE: Confirmar Encomenda Pendente ===");

            // Given
            System.out.println("Status inicial: " + encomendaTeste.getStatus());
            assertEquals(StatusEncomenda.PENDENTE, encomendaTeste.getStatus());

            // When
            encomendaTeste.confirmar();

            // Then
            System.out.println("Status após confirmação: " + encomendaTeste.getStatus());
            System.out.println("Data estimada calculada: " + encomendaTeste.getDataEstimadaEntrega());

            assertEquals(StatusEncomenda.CONFIRMADA, encomendaTeste.getStatus());
            assertNotNull(encomendaTeste.getDataEstimadaEntrega());

            System.out.println("✅ SUCESSO: Encomenda confirmada e data calculada!");
        }

        @Test
        @DisplayName("Deve calcular data estimada corretamente ao confirmar")
        void deveCalcularDataEstimadaCorretamenteAoConfirmar() {
            System.out.println("\n=== TESTE: Calcular Data Estimada por Tipo de Entrega ===");

            // Given
            LocalDate hoje = LocalDate.now();
            System.out.println("Data de hoje: " + hoje);

            // Test EXPRESSA (1 dia)
            System.out.println("\nTeste EXPRESSA (1 dia):");
            encomendaTeste.setTipoEntrega(TipoEntrega.EXPRESSA);
            encomendaTeste.confirmar();
            System.out.println("- Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            assertEquals(hoje.plusDays(1), encomendaTeste.getDataEstimadaEntrega());

            // Reset para PADRAO (3 dias)
            System.out.println("\nTeste PADRÃO (3 dias):");
            encomendaTeste.setStatus(StatusEncomenda.PENDENTE);
            encomendaTeste.setTipoEntrega(TipoEntrega.PADRAO);
            encomendaTeste.confirmar();
            System.out.println("- Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            assertEquals(hoje.plusDays(3), encomendaTeste.getDataEstimadaEntrega());

            // Reset para ECONOMICA (7 dias)
            System.out.println("\nTeste ECONÔMICA (7 dias):");
            encomendaTeste.setStatus(StatusEncomenda.PENDENTE);
            encomendaTeste.setTipoEntrega(TipoEntrega.ECONOMICA);
            encomendaTeste.confirmar();
            System.out.println("- Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            assertEquals(hoje.plusDays(7), encomendaTeste.getDataEstimadaEntrega());

            System.out.println("✅ SUCESSO: Todas as datas calculadas corretamente!");
        }

        @Test
        @DisplayName("Deve lançar exceção ao confirmar encomenda já confirmada")
        void deveLancarExcecaoAoConfirmarEncomendaJaConfirmada() {
            System.out.println("\n=== TESTE: Exceção ao Confirmar Encomenda Já Confirmada ===");

            // Given
            encomendaTeste.confirmar(); // Primeira confirmação
            System.out.println("Status após primeira confirmação: " + encomendaTeste.getStatus());

            // When & Then
            System.out.println("Tentando confirmar novamente...");
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> encomendaTeste.confirmar()
            );

            System.out.println("Exceção capturada: " + exception.getMessage());
            assertEquals("Só é possível confirmar encomendas pendentes", exception.getMessage());
            System.out.println("✅ SUCESSO: Exceção lançada corretamente!");
        }

        @Test
        @DisplayName("Deve iniciar trânsito de encomenda confirmada")
        void deveIniciarTransitoDeEncomendaConfirmada() {
            System.out.println("\n=== TESTE: Iniciar Trânsito de Encomenda Confirmada ===");

            // Given
            encomendaTeste.confirmar();
            System.out.println("Status após confirmação: " + encomendaTeste.getStatus());

            // When
            encomendaTeste.iniciarTransito();

            // Then
            System.out.println("Status após iniciar trânsito: " + encomendaTeste.getStatus());
            assertEquals(StatusEncomenda.EM_TRANSITO, encomendaTeste.getStatus());
            System.out.println("✅ SUCESSO: Trânsito iniciado corretamente!");
        }

        @Test
        @DisplayName("Deve lançar exceção ao iniciar trânsito de encomenda não confirmada")
        void deveLancarExcecaoAoIniciarTransitoDeEncomendaNaoConfirmada() {
            System.out.println("\n=== TESTE: Exceção ao Iniciar Trânsito sem Confirmação ===");

            System.out.println("Status atual: " + encomendaTeste.getStatus());
            System.out.println("Tentando iniciar trânsito sem confirmar...");

            // When & Then
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> encomendaTeste.iniciarTransito()
            );

            System.out.println("Exceção capturada: " + exception.getMessage());
            assertEquals("Só é possível iniciar trânsito de encomendas confirmadas", exception.getMessage());
            System.out.println("✅ SUCESSO: Exceção lançada corretamente!");
        }

        @Test
        @DisplayName("Deve finalizar entrega de encomenda em trânsito")
        void deveFinalizarEntregaDeEncomendaEmTransito() {
            System.out.println("\n=== TESTE: Finalizar Entrega de Encomenda em Trânsito ===");

            // Given
            encomendaTeste.confirmar();
            encomendaTeste.iniciarTransito();
            System.out.println("Status após iniciar trânsito: " + encomendaTeste.getStatus());

            // When
            encomendaTeste.finalizarEntrega();

            // Then
            System.out.println("Status após finalizar entrega: " + encomendaTeste.getStatus());
            System.out.println("Data entrega realizada: " + encomendaTeste.getDataEntregaRealizada());

            assertEquals(StatusEncomenda.ENTREGUE, encomendaTeste.getStatus());
            assertNotNull(encomendaTeste.getDataEntregaRealizada());
            assertTrue(encomendaTeste.getDataEntregaRealizada().isBefore(LocalDateTime.now().plusMinutes(1)));

            System.out.println("✅ SUCESSO: Entrega finalizada corretamente!");
        }

        @Test
        @DisplayName("Deve lançar exceção ao finalizar entrega de encomenda não em trânsito")
        void deveLancarExcecaoAoFinalizarEntregaDeEncomendaNaoEmTransito() {
            System.out.println("\n=== TESTE: Exceção ao Finalizar Entrega sem Trânsito ===");

            // Given - encomenda apenas confirmada
            encomendaTeste.confirmar();
            System.out.println("Status atual: " + encomendaTeste.getStatus());
            System.out.println("Tentando finalizar entrega sem estar em trânsito...");

            // When & Then
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> encomendaTeste.finalizarEntrega()
            );

            System.out.println("Exceção capturada: " + exception.getMessage());
            assertEquals("Só é possível finalizar encomendas em trânsito", exception.getMessage());
            System.out.println("✅ SUCESSO: Exceção lançada corretamente!");
        }

        @Test
        @DisplayName("Deve cancelar encomenda pendente com motivo")
        void deveCancelarEncomendaPendenteComMotivo() {
            System.out.println("\n=== TESTE: Cancelar Encomenda com Motivo ===");

            // Given
            String motivo = "Cliente desistiu da compra";
            System.out.println("Status inicial: " + encomendaTeste.getStatus());
            System.out.println("Motivo do cancelamento: " + motivo);

            // When
            encomendaTeste.cancelar(motivo);

            // Then
            System.out.println("Status após cancelamento: " + encomendaTeste.getStatus());
            System.out.println("Observações: " + encomendaTeste.getObservacoes());

            assertEquals(StatusEncomenda.CANCELADA, encomendaTeste.getStatus());
            assertTrue(encomendaTeste.getObservacoes().contains("CANCELAMENTO: " + motivo));
            System.out.println("✅ SUCESSO: Encomenda cancelada com motivo registrado!");
        }

        @Test
        @DisplayName("Deve cancelar encomenda sem sobrescrever observações existentes")
        void deveCancelarEncomendaSemSobrescreverObservacoesExistentes() {
            System.out.println("\n=== TESTE: Cancelar Encomenda Preservando Observações ===");

            // Given
            String observacaoInicial = "Entrega preferencial pela manhã";
            String motivo = "Problema com endereço";
            encomendaTeste.setObservacoes(observacaoInicial);

            System.out.println("Observação inicial: " + observacaoInicial);
            System.out.println("Motivo do cancelamento: " + motivo);

            // When
            encomendaTeste.cancelar(motivo);

            // Then
            System.out.println("Observações finais: " + encomendaTeste.getObservacoes());

            assertTrue(encomendaTeste.getObservacoes().contains(observacaoInicial));
            assertTrue(encomendaTeste.getObservacoes().contains("CANCELAMENTO: " + motivo));
            System.out.println("✅ SUCESSO: Observações preservadas e motivo adicionado!");
        }

        @Test
        @DisplayName("Deve lançar exceção ao cancelar encomenda já entregue")
        void deveLancarExcecaoAoCancelarEncomendaJaEntregue() {
            System.out.println("\n=== TESTE: Exceção ao Cancelar Encomenda Entregue ===");

            // Given - encomenda entregue
            encomendaTeste.confirmar();
            encomendaTeste.iniciarTransito();
            encomendaTeste.finalizarEntrega();
            System.out.println("Status da encomenda: " + encomendaTeste.getStatus());
            System.out.println("Tentando cancelar encomenda já entregue...");

            // When & Then
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> encomendaTeste.cancelar("Motivo qualquer")
            );

            System.out.println("Exceção capturada: " + exception.getMessage());
            assertEquals("Não é possível cancelar encomendas já entregues", exception.getMessage());
            System.out.println("✅ SUCESSO: Exceção lançada corretamente!");
        }
    }

    @Nested
    @DisplayName("Testes de Métodos Utilitários")
    class TestesMetodosUtilitarios {

        private Encomenda encomendaTeste;

        @BeforeEach
        void setUp() {
            encomendaTeste = new Encomenda(
                clienteTeste,
                enderecoOrigemTeste,
                enderecoDestinoTeste,
                TipoEntrega.PADRAO,
                "Bebê teste"
            );
        }

        @Test
        @DisplayName("Deve verificar se encomenda pode ser modificada")
        void deveVerificarSeEncomendaPodeSerModificada() {
            System.out.println("\n=== TESTE: Verificar se Encomenda Pode ser Modificada ===");

            // Given - encomenda pendente
            System.out.println("Status inicial: " + encomendaTeste.getStatus());
            System.out.println("Pode ser modificada? " + encomendaTeste.podeSerModificada());
            assertTrue(encomendaTeste.podeSerModificada());

            // When - confirmar encomenda
            encomendaTeste.confirmar();
            System.out.println("Status após confirmação: " + encomendaTeste.getStatus());
            System.out.println("Pode ser modificada? " + encomendaTeste.podeSerModificada());

            // Then - não pode mais ser modificada
            assertFalse(encomendaTeste.podeSerModificada());
            System.out.println("✅ SUCESSO: Verificação de modificação funcionando!");
        }

        @Test
        @DisplayName("Deve verificar se encomenda está ativa")
        void deveVerificarSeEncomendaEstaAtiva() {
            System.out.println("\n=== TESTE: Verificar se Encomenda Está Ativa ===");

            // Given - encomenda não cancelada
            System.out.println("Status inicial: " + encomendaTeste.getStatus());
            System.out.println("Está ativa? " + encomendaTeste.isAtiva());
            assertTrue(encomendaTeste.isAtiva());

            // When - cancelar encomenda
            encomendaTeste.cancelar("Teste");
            System.out.println("Status após cancelamento: " + encomendaTeste.getStatus());
            System.out.println("Está ativa? " + encomendaTeste.isAtiva());

            // Then - não está mais ativa
            assertFalse(encomendaTeste.isAtiva());
            System.out.println("✅ SUCESSO: Verificação de atividade funcionando!");
        }

        @Test
        @DisplayName("Deve verificar se encomenda foi entregue")
        void deveVerificarSeEncomendaFoiEntregue() {
            System.out.println("\n=== TESTE: Verificar se Encomenda Foi Entregue ===");

            // Given - encomenda não entregue
            System.out.println("Status inicial: " + encomendaTeste.getStatus());
            System.out.println("Foi entregue? " + encomendaTeste.isEntregue());
            assertFalse(encomendaTeste.isEntregue());

            // When - entregar encomenda
            encomendaTeste.confirmar();
            encomendaTeste.iniciarTransito();
            encomendaTeste.finalizarEntrega();

            System.out.println("Processando entrega...");
            System.out.println("Status final: " + encomendaTeste.getStatus());
            System.out.println("Foi entregue? " + encomendaTeste.isEntregue());

            // Then - está entregue
            assertTrue(encomendaTeste.isEntregue());
            System.out.println("✅ SUCESSO: Verificação de entrega funcionando!");
        }

        @Test
        @DisplayName("Deve verificar se encomenda está atrasada")
        void deveVerificarSeEncomendaEstaAtrasada() {
            System.out.println("\n=== TESTE: Verificar se Encomenda Está Atrasada ===");

            // Given - encomenda sem data estimada
            System.out.println("Data estimada: " + encomendaTeste.getDataEstimadaEntrega());
            System.out.println("Está atrasada? " + encomendaTeste.isAtrasada());
            assertFalse(encomendaTeste.isAtrasada());

            // When - confirmar e definir data no passado
            encomendaTeste.confirmar();
            LocalDate dataPassado = LocalDate.now().minusDays(1);
            encomendaTeste.setDataEstimadaEntrega(dataPassado);

            System.out.println("Data estimada alterada para: " + dataPassado);
            System.out.println("Data atual: " + LocalDate.now());
            System.out.println("Está atrasada? " + encomendaTeste.isAtrasada());

            // Then - está atrasada
            assertTrue(encomendaTeste.isAtrasada());
            System.out.println("✅ SUCESSO: Verificação de atraso funcionando!");
        }

        @Test
        @DisplayName("Encomenda entregue não deve ser considerada atrasada")
        void encomendaEntregueNaoDeveSerConsideradaAtrasada() {
            System.out.println("\n=== TESTE: Encomenda Entregue Não é Atrasada ===");

            // Given - encomenda entregue com data no passado
            encomendaTeste.confirmar();
            LocalDate dataPassado = LocalDate.now().minusDays(1);
            encomendaTeste.setDataEstimadaEntrega(dataPassado);
            encomendaTeste.iniciarTransito();
            encomendaTeste.finalizarEntrega();

            System.out.println("Data estimada (passado): " + dataPassado);
            System.out.println("Status: " + encomendaTeste.getStatus());
            System.out.println("Está atrasada? " + encomendaTeste.isAtrasada());

            // Then - não está atrasada pois foi entregue
            assertFalse(encomendaTeste.isAtrasada());
            System.out.println("✅ SUCESSO: Encomendas entregues não são consideradas atrasadas!");
        }

        @Test
        @DisplayName("Deve verificar se é entrega interestadual")
        void deveVerificarSeEEntregaInterestadual() {
            System.out.println("\n=== TESTE: Verificar Entrega Interestadual ===");

            // Given - origem e destino no mesmo estado (SP)
            System.out.println("Origem: " + enderecoOrigemTeste.getUf());
            System.out.println("Destino: " + enderecoDestinoTeste.getUf());
            System.out.println("É interestadual? " + encomendaTeste.isEntregaInterestadual());
            assertFalse(encomendaTeste.isEntregaInterestadual());

            // When - alterar destino para outro estado
            Endereco enderecoRJ = new Endereco(
                "20040-020", "Rua da Carioca", "100", "Centro", "Rio de Janeiro", UF.RJ
            );
            encomendaTeste.setEnderecoDestino(enderecoRJ);

            System.out.println("\nApós alterar destino:");
            System.out.println("Novo destino: " + enderecoRJ.getUf());
            System.out.println("É interestadual? " + encomendaTeste.isEntregaInterestadual());

            // Then - é interestadual
            assertTrue(encomendaTeste.isEntregaInterestadual());
            System.out.println("✅ SUCESSO: Verificação interestadual funcionando!");
        }

        @Test
        @DisplayName("Deve calcular valor total com frete")
        void deveCalcularValorTotalComFrete() {
            System.out.println("\n=== TESTE: Calcular Valor Total com Frete ===");

            // Given
            BigDecimal valorDeclarado = new BigDecimal("200.00");
            encomendaTeste.setValorDeclarado(valorDeclarado);
            System.out.println("Valor declarado: R$ " + valorDeclarado);

            // Criar frete mock
            Frete frete = new Frete(
                encomendaTeste,
                TipoEntrega.PADRAO,
                new BigDecimal("25.50"),
                new BigDecimal("50.0"),
                3
            );
            encomendaTeste.setFrete(frete);
            System.out.println("Valor do frete: R$ " + frete.getValor());

            // When
            BigDecimal valorTotal = encomendaTeste.getValorTotal();

            // Then
            System.out.println("Valor total calculado: R$ " + valorTotal);
            assertEquals(new BigDecimal("225.50"), valorTotal);
            System.out.println("✅ SUCESSO: Valor total calculado corretamente!");
        }

        @Test
        @DisplayName("Deve retornar null para valor total sem frete")
        void deveRetornarNullParaValorTotalSemFrete() {
            System.out.println("\n=== TESTE: Valor Total sem Frete ===");

            // Given - encomenda sem frete
            System.out.println("Frete associado: " + encomendaTeste.getFrete());
            assertNull(encomendaTeste.getFrete());

            // When
            BigDecimal valorTotal = encomendaTeste.getValorTotal();

            // Then
            System.out.println("Valor total retornado: " + valorTotal);
            assertNull(valorTotal);
            System.out.println("✅ SUCESSO: Retorna null quando não há frete!");
        }

        @Test
        @DisplayName("Deve gerar resumo formatado")
        void deveGerarResumoFormatado() {
            System.out.println("\n=== TESTE: Gerar Resumo Formatado ===");

            // When
            String resumo = encomendaTeste.getResumo();

            // Then
            System.out.println("Resumo gerado: " + resumo);
            System.out.println("Contém código? " + resumo.contains(encomendaTeste.getCodigo()));
            System.out.println("Contém origem? " + resumo.contains("Mogi Guaçu"));
            System.out.println("Contém destino? " + resumo.contains("São Paulo"));
            System.out.println("Contém status? " + resumo.contains("Pendente"));

            assertTrue(resumo.contains(encomendaTeste.getCodigo()));
            assertTrue(resumo.contains("Mogi Guaçu"));
            assertTrue(resumo.contains("São Paulo"));
            assertTrue(resumo.contains("Pendente"));
            System.out.println("✅ SUCESSO: Resumo formatado corretamente!");
        }
    }

    @Nested
    @DisplayName("Testes de Callbacks JPA")
    class TestesCallbacksJPA {

        @Test
        @DisplayName("Deve inicializar campos obrigatórios no @PrePersist")
        void deveInicializarCamposObrigatoriosNoPrePersist() {
            System.out.println("\n=== TESTE: Callback @PrePersist ===");

            // Given
            Encomenda encomenda = new Encomenda();
            encomenda.setCliente(clienteTeste);
            encomenda.setEnderecoOrigem(enderecoOrigemTeste);
            encomenda.setEnderecoDestino(enderecoDestinoTeste);
            encomenda.setTipoEntrega(TipoEntrega.PADRAO);
            encomenda.setDescricaoBebe("Bebê teste");

            System.out.println("Antes do @PrePersist:");
            System.out.println("- Data pedido: " + encomenda.getDataPedido());
            System.out.println("- Status: " + encomenda.getStatus());
            System.out.println("- Código: " + encomenda.getCodigo());

            // When
            encomenda.onCreate(); // Simula @PrePersist

            // Then
            System.out.println("\nApós @PrePersist:");
            System.out.println("- Data pedido: " + encomenda.getDataPedido());
            System.out.println("- Status: " + encomenda.getStatus());
            System.out.println("- Código: " + encomenda.getCodigo());

            assertNotNull(encomenda.getDataPedido());
            assertEquals(StatusEncomenda.PENDENTE, encomenda.getStatus());
            assertNotNull(encomenda.getCodigo());
            assertFalse(encomenda.getCodigo().trim().isEmpty());
            System.out.println("✅ SUCESSO: Campos inicializados pelo @PrePersist!");
        }

        @Test
        @DisplayName("Deve normalizar observações no @PreUpdate")
        void deveNormalizarObservacoesNoPreUpdate() {
            System.out.println("\n=== TESTE: Callback @PreUpdate - Normalização ===");

            // Given
            Encomenda encomenda = new Encomenda(
                clienteTeste, enderecoOrigemTeste, enderecoDestinoTeste,
                TipoEntrega.PADRAO, "Bebê teste"
            );
            String observacaoComEspacos = "   Observação com espaços   ";
            encomenda.setObservacoes(observacaoComEspacos);

            System.out.println("Observação antes do @PreUpdate: '" + observacaoComEspacos + "'");

            // When
            encomenda.onUpdate(); // Simula @PreUpdate

            // Then
            System.out.println("Observação após @PreUpdate: '" + encomenda.getObservacoes() + "'");
            assertEquals("Observação com espaços", encomenda.getObservacoes());
            System.out.println("✅ SUCESSO: Observações normalizadas pelo @PreUpdate!");
        }

        @Test
        @DisplayName("Deve definir observações como null se estiver vazia")
        void deveDefinirObservacoesComoNullSeEstiverVazia() {
            System.out.println("\n=== TESTE: Callback @PreUpdate - Observação Vazia ===");

            // Given
            Encomenda encomenda = new Encomenda(
                clienteTeste, enderecoOrigemTeste, enderecoDestinoTeste,
                TipoEntrega.PADRAO, "Bebê teste"
            );
            String observacaoVazia = "   ";
            encomenda.setObservacoes(observacaoVazia);

            System.out.println("Observação antes (só espaços): '" + observacaoVazia + "'");

            // When
            encomenda.onUpdate();

            // Then
            System.out.println("Observação após @PreUpdate: " + encomenda.getObservacoes());
            assertNull(encomenda.getObservacoes());
            System.out.println("✅ SUCESSO: Observação vazia convertida para null!");
        }
    }

    @Nested
    @DisplayName("Testes de Validação")
    class TestesValidacao {

        @Test
        @DisplayName("Deve aceitar encomenda com dados válidos")
        void deveAceitarEncomendaComDadosValidos() {
            System.out.println("\n=== TESTE: Validação com Dados Válidos ===");

            // Given & When
            String descricao = "Bebê reborn Maria, 48cm, cabelo cacheado, olhos azuis";
            BigDecimal peso = new BigDecimal("3.2");
            BigDecimal altura = new BigDecimal("48.0");
            BigDecimal valorDeclarado = new BigDecimal("450.00");

            System.out.println("Dados de entrada:");
            System.out.println("- Descrição: " + descricao);
            System.out.println("- Peso: " + peso + " kg");
            System.out.println("- Altura: " + altura + " cm");
            System.out.println("- Valor declarado: R$ " + valorDeclarado);

            Encomenda encomenda = new Encomenda(
                clienteTeste,
                enderecoOrigemTeste,
                enderecoDestinoTeste,
                TipoEntrega.EXPRESSA,
                descricao,
                peso,
                altura,
                valorDeclarado
            );

            // Then
            System.out.println("\nDados gravados:");
            System.out.println("- Descrição: " + encomenda.getDescricaoBebe());
            System.out.println("- Peso: " + encomenda.getPesoKg() + " kg");
            System.out.println("- Altura: " + encomenda.getAlturaCm() + " cm");
            System.out.println("- Valor: R$ " + encomenda.getValorDeclarado());

            assertNotNull(encomenda);
            assertEquals(descricao, encomenda.getDescricaoBebe());
            assertEquals(peso, encomenda.getPesoKg());
            assertEquals(altura, encomenda.getAlturaCm());
            assertEquals(valorDeclarado, encomenda.getValorDeclarado());
            System.out.println("✅ SUCESSO: Encomenda aceita com dados válidos!");
        }

        @Test
        @DisplayName("Deve aceitar encomenda sem campos opcionais")
        void deveAceitarEncomendaSemCamposOpcionais() {
            System.out.println("\n=== TESTE: Validação sem Campos Opcionais ===");

            // Given & When
            String descricao = "Bebê simples para teste";
            System.out.println("Criando encomenda apenas com campos obrigatórios:");
            System.out.println("- Descrição: " + descricao);
            System.out.println("- Campos opcionais: não informados");

            Encomenda encomenda = new Encomenda(
                clienteTeste,
                enderecoOrigemTeste,
                enderecoDestinoTeste,
                TipoEntrega.PADRAO,
                descricao
            );

            // Then
            System.out.println("\nCampos opcionais verificados:");
            System.out.println("- Peso: " + encomenda.getPesoKg());
            System.out.println("- Altura: " + encomenda.getAlturaCm());
            System.out.println("- Valor declarado: " + encomenda.getValorDeclarado());
            System.out.println("- Observações: " + encomenda.getObservacoes());

            assertNotNull(encomenda);
            assertNull(encomenda.getPesoKg());
            assertNull(encomenda.getAlturaCm());
            assertNull(encomenda.getValorDeclarado());
            assertNull(encomenda.getObservacoes());
            System.out.println("✅ SUCESSO: Encomenda criada sem campos opcionais!");
        }
    }
}
