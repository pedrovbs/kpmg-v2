package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.dto.CompraRequest;
import com.upoiny.kpmgv2.dto.ItemCompraRequest;
import com.upoiny.kpmgv2.entities.*;
import com.upoiny.kpmgv2.repositories.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public Page<Compra> listar(Pageable pageable){

        return compraRepository.findAll(pageable);

    }

    public Page<Compra> pesquisarPorStatus(String status, Pageable pageable) {
        return compraRepository.findByStatusContainingIgnoreCase(status, pageable);
    }

    // ======================================
    // Buscar compra por ID
    // ======================================

    @Transactional
    public Compra buscarPorId(Long id){

        return compraRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Compra não encontrada."
                        )
                );

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
