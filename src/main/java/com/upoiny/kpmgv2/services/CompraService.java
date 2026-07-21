package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.dto.*;
import com.upoiny.kpmgv2.entities.*;
import com.upoiny.kpmgv2.repositories.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {


    @Autowired
    private CompraRepository compraRepository;


    @Autowired
    private ProdutoRepository produtoRepository;


    @Autowired
    private FornecedorRepository fornecedorRepository;


    @Autowired
    private FuncionarioRepository funcionarioRepository;



    @Transactional
    public Compra realizarCompra(CompraRequest request) {

        // ==============================
        // Buscar fornecedor
        // ==============================

        Fornecedor fornecedor =
                fornecedorRepository.findById(request.getFornecedorId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Fornecedor não encontrado."
                                )
                        );
        // ==============================
        // Buscar funcionário responsável
        // ==============================

        Funcionario funcionario =
                funcionarioRepository.findById((long) Math.toIntExact(request.getFuncionarioId()))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Funcionário não encontrado."
                                )
                        );
        // ==============================
        // Criar compra
        // ==============================

        Compra compra = new Compra();

        compra.setFornecedor(fornecedor);

        compra.setFuncionario(funcionario);

        compra.setDataCompra(LocalDateTime.now());

        compra.setStatus("FINALIZADA");

        compra.setObservacoes(
                request.getObservacoes()
        );

        Double valorTotal = 0.0;

        // ==============================
        // Processar itens
        // ==============================

        for(ItemCompraRequest itemRequest : request.getItens()){


            Produto produto =
                    produtoRepository.findById(
                                    itemRequest.getProdutoId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Produto não encontrado."
                                    )
                            );



            // ==============================
            // Validar preço de compra
            // ==============================

            if(produto.getPrecoCompra() == null){

                throw new RuntimeException(
                        "Produto sem preço de compra cadastrado."
                );

            }


            // ==============================
            // Calcular subtotal
            // ==============================

            Double subtotal =
                    produto.getPrecoCompra()
                            *
                            itemRequest.getQuantidade();



            // ==============================
            // Criar ItemCompra
            // ==============================

            ItemCompra itemCompra = new ItemCompra();

            itemCompra.setCompra(compra);

            itemCompra.setProduto(produto);

            itemCompra.setQuantidade(
                    itemRequest.getQuantidade()
            );

            itemCompra.setValorUnitario(
                    produto.getPrecoCompra()
            );

            itemCompra.setSubtotal(
                    subtotal
            );



            // ==============================
            // Adicionar item na compra
            // ==============================

            compra.getItens()
                    .add(itemCompra);



            // ==============================
            // Atualizar estoque
            // ==============================

            Integer novoEstoque =
                    produto.getEstoque()
                            +
                            itemRequest.getQuantidade();


            produto.setEstoque(novoEstoque);


            produtoRepository.save(produto);



            // ==============================
            // Somar valor total
            // ==============================

            valorTotal += subtotal;

        }



        // ==============================
        // Finalizar compra
        // ==============================

        compra.setValorTotal(valorTotal);



        return compraRepository.save(compra);

    }


    // ======================================
    // Consultar todas as compras
    // ======================================

    @Transactional
    public Page<CompraResponse> listar(Pageable pageable) {
        return compraRepository
                .findAll(pageable)
                .map(this::converterParaResponse);
    }

    @Transactional
    public Page<CompraResponse> pesquisarPorStatus(
            String status,
            Pageable pageable
    ) {
        return compraRepository
                .findByStatusContainingIgnoreCase(status, pageable)
                .map(this::converterParaResponse);
    }

    private CompraResponse converterParaResponse(Compra compra) {

        CompraResponse response = new CompraResponse();

        response.setId(compra.getId());
        response.setDataCompra(compra.getDataCompra());
        response.setValorTotal(compra.getValorTotal());
        response.setStatus(compra.getStatus());
        response.setObservacoes(compra.getObservacoes());

        if (compra.getFornecedor() != null) {
            response.setFornecedorId(
                    compra.getFornecedor().getId()
            );

            response.setFornecedorNome(
                    compra.getFornecedor().getNomeFantasia()
            );
        }

        if (compra.getFuncionario() != null) {
            response.setFuncionarioId(
                    compra.getFuncionario().getId()
            );

            response.setFuncionarioNome(
                    compra.getFuncionario().getNome()
            );
        }

        return response;
    }

    public Long contarTotalCompras() {
        return compraRepository.contarTotalCompras();
    }

    public List<FornecedorCompraKpiResponse> buscarFornecedoresComMaisCompras() {

        return compraRepository.buscarFornecedoresComMaisCompras();
    }
    // ======================================
    // Buscar compra por ID
    // ======================================

    public List<ProdutoCompraKpiResponse> buscarProdutosMaisComprados() {

        return compraRepository.buscarProdutosMaisComprados(
                PageRequest.of(0, 10)
        );
    }
    @Transactional
    public Compra buscarPorId(Long id){

        return compraRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Compra não encontrada."
                        )
                );

    }

    public long contarClientes() {
        return compraRepository.count();
    }

    // ======================================
    // Cancelar compra
    // ======================================

    @Transactional
    public void cancelarCompra(Long id){

        Compra compra = buscarPorId(id);

        if(compra.getStatus()
                .equals("CANCELADA")){
            throw new RuntimeException(
                    "Compra já está cancelada."
            );

        }
        /*
         * Ao cancelar uma compra,
         * devolvemos os produtos ao estoque.
         */

        for(ItemCompra item : compra.getItens()){
            Produto produto =
                    item.getProduto();
            produto.setEstoque(
                    produto.getEstoque()
                            -
                            item.getQuantidade()
            );
            produtoRepository.save(produto);
        }
        compra.setStatus("CANCELADA");
        compraRepository.save(compra);

    }

    @Transactional
    public Compra atualizar(Long id, Compra atualizada) {
        Compra compra = buscarPorId(id);
        compra.setStatus(atualizada.getStatus());
        compra.setObservacoes(atualizada.getObservacoes());
        return compraRepository.save(compra);
    }

    @Transactional
    public void excluir(Long id) {
        compraRepository.delete(buscarPorId(id));
    }

}
