package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.dto.*;
import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.entities.ItemVenda;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.repositories.ClienteRepository;
import com.upoiny.kpmgv2.repositories.FuncionarioRepository;
import com.upoiny.kpmgv2.repositories.ProdutoRepository;
import com.upoiny.kpmgv2.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository,
                        ClienteRepository clienteRepository,
                        FuncionarioRepository funcionarioRepository,
                        ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
    }

    public Page<VendaResponse> listar(Pageable pageable) {

        return vendaRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }
    private VendaResponse converterParaResponse(Venda venda) {

        List<ItemVendaResponse> itens =
                venda.getItens()
                        .stream()
                        .map(item -> new ItemVendaResponse(
                                item.getId(),
                                item.getProduto().getId(),
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getSubtotal()
                        ))
                        .toList();

        return new VendaResponse(
                venda.getId(),
                venda.getDataVenda(),
                venda.getValorTotal(),
                venda.getDesconto(),
                venda.getValorFinal(),
                venda.getFormaPagamento(),
                venda.getStatus(),
                venda.getObservacoes(),
                venda.getCliente().getId(),
                venda.getCliente().getNome(),
                venda.getFuncionario().getId(),
                venda.getFuncionario().getNome(),
                itens
        );
    }


    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada."));
    }

    public VendaResponse buscarPorNumero(Long numeroPedido) {

        Venda venda = vendaRepository.findById(numeroPedido)
                .orElseThrow(() ->
                        new RuntimeException("Venda não encontrada.")
                );

        return converterParaResponse(venda);
    }


    public long contarVendas() {
        return vendaRepository.count();
    }

    public Page<Venda> listarPorCliente(Cliente cliente, Pageable pageable) {
        return vendaRepository.findByCliente(cliente, pageable);
    }

    public Page<Venda> listarPorFuncionario(Funcionario funcionario, Pageable pageable) {
        return vendaRepository.findByFuncionario(funcionario, pageable);
    }

    public Page<Venda> listarPorStatus(String status, Pageable pageable) {
        return vendaRepository.findByStatusContainingIgnoreCase(status, pageable);
    }

    public Page<Venda> listarPorFormaPagamento(String formaPagamento, Pageable pageable) {
        return vendaRepository.findByFormaPagamentoContainingIgnoreCase(formaPagamento, pageable);
    }

    @Transactional
    public Venda realizarVenda(VendaRequest request) {

        System.out.println("========== INÍCIO DA VENDA ==========");

        // =====================================================
        // 1. VALIDAR REQUEST
        // =====================================================

        if (request == null) {
            throw new RuntimeException(
                    "ERRO DEBUG: O objeto VendaRequest está NULL."
            );
        }

        System.out.println(
                "DEBUG REQUEST -> Cliente ID: "
                        + request.getClienteId()
                        + " | Funcionário ID: "
                        + request.getFuncionarioId()
                        + " | Forma Pagamento: "
                        + request.getFormaPagamento()
                        + " | Desconto: "
                        + request.getDesconto()
        );

        if (request.getClienteId() == null) {
            throw new RuntimeException(
                    "ERRO DEBUG: clienteId está NULL."
            );
        }

        if (request.getFuncionarioId() == null) {
            throw new RuntimeException(
                    "ERRO DEBUG: funcionarioId está NULL."
            );
        }

        if (request.getItens() == null) {
            throw new RuntimeException(
                    "ERRO DEBUG: A lista de itens está NULL."
            );
        }

        if (request.getItens().isEmpty()) {
            throw new RuntimeException(
                    "ERRO DEBUG: A venda não possui itens."
            );
        }

        if (request.getFormaPagamento() == null
                || request.getFormaPagamento().isBlank()) {

            throw new RuntimeException(
                    "ERRO DEBUG: A forma de pagamento está vazia."
            );
        }

        System.out.println(
                "DEBUG REQUEST -> Quantidade de itens: "
                        + request.getItens().size()
        );


        // =====================================================
        // 2. BUSCAR CLIENTE
        // =====================================================

        Cliente cliente = clienteRepository.findById(
                request.getClienteId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "ERRO DEBUG: Cliente não encontrado. ID: "
                                + request.getClienteId()
                )
        );

        System.out.println(
                "DEBUG CLIENTE -> "
                        + "ID: " + cliente.getId()
                        + " | Nome: " + cliente.getNome()
        );


        // =====================================================
        // 3. BUSCAR FUNCIONÁRIO
        // =====================================================

        Funcionario funcionario = funcionarioRepository.findById(
                request.getFuncionarioId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "ERRO DEBUG: Funcionário não encontrado. ID: "
                                + request.getFuncionarioId()
                )
        );

        System.out.println(
                "DEBUG FUNCIONARIO -> "
                        + "ID: " + funcionario.getId()
                        + " | Nome: " + funcionario.getNome()
        );


        // =====================================================
        // 4. CRIAR VENDA
        // =====================================================

        Venda venda = new Venda();

        venda.setCliente(cliente);
        venda.setFuncionario(funcionario);
        venda.setDataVenda(LocalDateTime.now());
        venda.setFormaPagamento(request.getFormaPagamento());
        venda.setStatus("FINALIZADA");
        venda.setObservacoes(request.getObservacoes());

        if (venda.getItens() == null) {
            throw new RuntimeException(
                    "ERRO DEBUG: venda.getItens() está NULL."
            );
        }

        System.out.println(
                "DEBUG VENDA -> Venda criada em memória."
        );


        // =====================================================
        // 5. PROCESSAR ITENS
        // =====================================================

        double valorTotal = 0.0;

        int contadorItem = 0;

        for (ItemVendaRequest itemRequest : request.getItens()) {

            contadorItem++;

            System.out.println(
                    "========== PROCESSANDO ITEM "
                            + contadorItem
                            + " =========="
            );


            // -----------------------------------------------
            // Validar itemRequest
            // -----------------------------------------------

            if (itemRequest == null) {
                throw new RuntimeException(
                        "ERRO DEBUG: Item "
                                + contadorItem
                                + " está NULL."
                );
            }

            if (itemRequest.getProdutoId() == null) {
                throw new RuntimeException(
                        "ERRO DEBUG: Produto ID do item "
                                + contadorItem
                                + " está NULL."
                );
            }

            if (itemRequest.getQuantidade() == null) {
                throw new RuntimeException(
                        "ERRO DEBUG: Quantidade do item "
                                + contadorItem
                                + " está NULL."
                );
            }

            if (itemRequest.getQuantidade() <= 0) {
                throw new RuntimeException(
                        "ERRO DEBUG: Quantidade inválida no item "
                                + contadorItem
                                + ". Quantidade: "
                                + itemRequest.getQuantidade()
                );
            }


            System.out.println(
                    "DEBUG ITEM REQUEST -> "
                            + "Produto ID: "
                            + itemRequest.getProdutoId()
                            + " | Quantidade: "
                            + itemRequest.getQuantidade()
            );


            // -----------------------------------------------
            // Buscar Produto
            // -----------------------------------------------

            Produto produto = produtoRepository.findById(
                    itemRequest.getProdutoId()
            ).orElseThrow(() ->
                    new RuntimeException(
                            "ERRO DEBUG: Produto não encontrado. ID: "
                                    + itemRequest.getProdutoId()
                    )
            );


            System.out.println(
                    "DEBUG PRODUTO -> "
                            + "ID: " + produto.getId()
                            + " | Nome: " + produto.getNome()
                            + " | Preço Venda: "
                            + produto.getPrecoVenda()
                            + " | Estoque: "
                            + produto.getEstoque()
            );


            // -----------------------------------------------
            // Validar preço
            // -----------------------------------------------

            if (produto.getPrecoVenda() == null) {

                throw new RuntimeException(
                        "ERRO DEBUG: Produto "
                                + produto.getId()
                                + " está sem preço de venda."
                );
            }

            if (produto.getPrecoVenda() < 0) {

                throw new RuntimeException(
                        "ERRO DEBUG: Produto "
                                + produto.getId()
                                + " possui preço de venda negativo: "
                                + produto.getPrecoVenda()
                );
            }


            // -----------------------------------------------
            // Validar estoque
            // -----------------------------------------------

            if (produto.getEstoque() == null) {

                throw new RuntimeException(
                        "ERRO DEBUG: Produto "
                                + produto.getId()
                                + " está com estoque NULL."
                );
            }

            if (produto.getEstoque() < itemRequest.getQuantidade()) {

                throw new RuntimeException(
                        "ERRO DEBUG: Estoque insuficiente. "
                                + "Produto: "
                                + produto.getNome()
                                + " | Estoque atual: "
                                + produto.getEstoque()
                                + " | Quantidade solicitada: "
                                + itemRequest.getQuantidade()
                );
            }


            // =================================================
            // CALCULAR SUBTOTAL
            // =================================================

            double precoUnitario = produto.getPrecoVenda();

            double subtotal =
                    precoUnitario
                            * itemRequest.getQuantidade();


            System.out.println(
                    "DEBUG CÁLCULO -> "
                            + "Preço Unitário: "
                            + precoUnitario
                            + " | Quantidade: "
                            + itemRequest.getQuantidade()
                            + " | Subtotal: "
                            + subtotal
            );


            if (Double.isNaN(precoUnitario)
                    || Double.isInfinite(precoUnitario)) {

                throw new RuntimeException(
                        "ERRO DEBUG: Preço unitário inválido: "
                                + precoUnitario
                );
            }

            if (Double.isNaN(subtotal)
                    || Double.isInfinite(subtotal)) {

                throw new RuntimeException(
                        "ERRO DEBUG: Subtotal inválido: "
                                + subtotal
                );
            }


            // =================================================
            // CRIAR ITEM VENDA
            // =================================================

            ItemVenda item = new ItemVenda();

            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setSubtotal(subtotal);


            // =================================================
            // VALIDAR ITEM VENDA
            // =================================================

            if (item.getVenda() == null) {

                throw new RuntimeException(
                        "ERRO DEBUG: ItemVenda sem Venda."
                );
            }

            if (item.getProduto() == null) {

                throw new RuntimeException(
                        "ERRO DEBUG: ItemVenda sem Produto."
                );
            }

            if (item.getQuantidade() == null
                    || item.getQuantidade() <= 0) {

                throw new RuntimeException(
                        "ERRO DEBUG: ItemVenda com quantidade inválida."
                );
            }

            if (item.getPrecoUnitario() == null) {

                throw new RuntimeException(
                        "ERRO DEBUG: ItemVenda com precoUnitario NULL."
                );
            }

            if (item.getSubtotal() == null) {

                throw new RuntimeException(
                        "ERRO DEBUG: ItemVenda com subtotal NULL."
                );
            }


            System.out.println(
                    "DEBUG ITEM VENDA -> "
                            + "Produto ID: "
                            + item.getProduto().getId()
                            + " | Quantidade: "
                            + item.getQuantidade()
                            + " | Preço Unitário: "
                            + item.getPrecoUnitario()
                            + " | Subtotal: "
                            + item.getSubtotal()
            );


            // =================================================
            // ADICIONAR ITEM À VENDA
            // =================================================

            venda.getItens().add(item);

            System.out.println(
                    "DEBUG LISTA VENDA -> "
                            + "Quantidade de itens na venda: "
                            + venda.getItens().size()
            );


            // =================================================
            // ATUALIZAR ESTOQUE
            // =================================================

            Integer estoqueAnterior = produto.getEstoque();

            produto.setEstoque(
                    produto.getEstoque()
                            - itemRequest.getQuantidade()
            );

            System.out.println(
                    "DEBUG ESTOQUE -> "
                            + "Produto ID: "
                            + produto.getId()
                            + " | Estoque anterior: "
                            + estoqueAnterior
                            + " | Estoque novo: "
                            + produto.getEstoque()
            );

            produtoRepository.save(produto);


            // =================================================
            // ATUALIZAR TOTAL
            // =================================================

            valorTotal += subtotal;

            System.out.println(
                    "DEBUG TOTAL PARCIAL -> "
                            + "Valor Total: "
                            + valorTotal
            );
        }


        // =====================================================
        // 6. VALIDAR TOTAL
        // =====================================================

        System.out.println(
                "========== TOTAL FINAL =========="
        );

        System.out.println(
                "DEBUG TOTAL FINAL -> "
                        + "Valor Total: "
                        + valorTotal
        );

        if (valorTotal < 0) {

            throw new RuntimeException(
                    "ERRO DEBUG: Valor total negativo: "
                            + valorTotal
            );
        }


        // =====================================================
        // 7. DESCONTO
        // =====================================================

        double desconto =
                request.getDesconto() == null
                        ? 0.0
                        : request.getDesconto();


        System.out.println(
                "DEBUG DESCONTO -> "
                        + "Desconto recebido: "
                        + desconto
                        + " | Valor Total: "
                        + valorTotal
        );


        if (Double.isNaN(desconto)
                || Double.isInfinite(desconto)) {

            throw new RuntimeException(
                    "ERRO DEBUG: Desconto inválido: "
                            + desconto
            );
        }

        if (desconto < 0) {

            throw new RuntimeException(
                    "ERRO DEBUG: Desconto negativo: "
                            + desconto
            );
        }

        if (desconto > valorTotal) {

            throw new RuntimeException(
                    "ERRO DEBUG: Desconto maior que o valor total. "
                            + "Desconto: "
                            + desconto
                            + " | Valor Total: "
                            + valorTotal
            );
        }


        // =====================================================
        // 8. CALCULAR TOTAIS DA VENDA
        // =====================================================

        double valorFinal =
                valorTotal - desconto;


        System.out.println(
                "DEBUG VALORES FINAIS -> "
                        + "Valor Total: "
                        + valorTotal
                        + " | Desconto: "
                        + desconto
                        + " | Valor Final: "
                        + valorFinal
        );


        venda.setValorTotal(valorTotal);
        venda.setDesconto(desconto);
        venda.setValorFinal(valorFinal);


        // =====================================================
        // 9. VALIDAÇÃO FINAL DA VENDA
        // =====================================================

        if (venda.getCliente() == null) {

            throw new RuntimeException(
                    "ERRO DEBUG: Venda sem cliente."
            );
        }

        if (venda.getFuncionario() == null) {

            throw new RuntimeException(
                    "ERRO DEBUG: Venda sem funcionário."
            );
        }

        if (venda.getItens() == null
                || venda.getItens().isEmpty()) {

            throw new RuntimeException(
                    "ERRO DEBUG: Venda sem itens."
            );
        }

        if (venda.getValorTotal() == null) {

            throw new RuntimeException(
                    "ERRO DEBUG: Valor total da venda está NULL."
            );
        }

        if (venda.getValorFinal() == null) {

            throw new RuntimeException(
                    "ERRO DEBUG: Valor final da venda está NULL."
            );
        }


        // =====================================================
        // 10. DEBUG FINAL ANTES DO SAVE
        // =====================================================

        System.out.println(
                "========== ANTES DO SAVE =========="
        );

        System.out.println(
                "DEBUG VENDA FINAL -> "
                        + "Cliente: "
                        + venda.getCliente().getId()
                        + " | Funcionário: "
                        + venda.getFuncionario().getId()
                        + " | Itens: "
                        + venda.getItens().size()
                        + " | Valor Total: "
                        + venda.getValorTotal()
                        + " | Desconto: "
                        + venda.getDesconto()
                        + " | Valor Final: "
                        + venda.getValorFinal()
        );

        for (ItemVenda item : venda.getItens()) {

            System.out.println(
                    "DEBUG ITEM FINAL -> "
                            + "Produto: "
                            + item.getProduto().getId()
                            + " | Quantidade: "
                            + item.getQuantidade()
                            + " | Preço Unitário: "
                            + item.getPrecoUnitario()
                            + " | Subtotal: "
                            + item.getSubtotal()
            );
        }


        // =====================================================
        // 11. SALVAR VENDA
        // =====================================================

        Venda vendaSalva = vendaRepository.save(venda);

        System.out.println(
                "========== VENDA SALVA COM SUCESSO =========="
        );

        System.out.println(
                "DEBUG VENDA SALVA -> ID: "
                        + vendaSalva.getId()
        );

        return vendaSalva;
    }

    public List<ClienteVendaKpiResponse> buscarClientesQueMaisCompraram() {

        return vendaRepository.buscarClientesQueMaisCompraram(
                PageRequest.of(0, 1)
        );
    }

    public VendaValorKpiResponse buscarResumoFinanceiroVendas() {

        return vendaRepository.buscarResumoFinanceiroVendas();
    }

    public List<ProdutoVendaKpiResponse> buscarProdutosMaisVendidos() {

        return vendaRepository.buscarProdutosMaisVendidos(
                PageRequest.of(0, 10)
        );
    }
    @Transactional
    public Venda atualizar(Long id, Venda atualizada) {
        Venda venda = buscarPorId(id);
        venda.setFormaPagamento(atualizada.getFormaPagamento());
        venda.setObservacoes(atualizada.getObservacoes());
        return vendaRepository.save(venda);
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        for (ItemVenda item : venda.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }
        vendaRepository.delete(venda);
    }

    public ClienteMaiorCompradorDTO obterClienteMaiorComprador() {

        return vendaRepository
                .findClienteComMaisCompras(PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElse(null);

    }
}
