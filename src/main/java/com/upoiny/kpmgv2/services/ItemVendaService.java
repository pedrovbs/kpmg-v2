package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.ItemVenda;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.repositories.ItemVendaRepository;
import com.upoiny.kpmgv2.repositories.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ItemVendaService {


    @Autowired
    private ItemVendaRepository repository;


    @Autowired
    private ProdutoRepository produtoRepository;



    public Page<ItemVenda> listar(Pageable pageable) {

        return repository.findAll(pageable);

    }



    public Page<ItemVenda> pesquisarPorProduto(
            String search,
            Pageable pageable) {

        return repository.findByProdutoNomeContainingIgnoreCase(
                search,
                pageable
        );
    }



    public ItemVenda buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Item da venda não encontrado."
                        ));

    }



    public Page<ItemVenda> listarPorVenda(
            Venda venda,
            Pageable pageable) {

        return repository.findByVenda(
                venda,
                pageable
        );

    }



    public Page<ItemVenda> listarPorProduto(
            Produto produto,
            Pageable pageable) {

        return repository.findByProduto(
                produto,
                pageable
        );

    }



    public ItemVenda salvar(ItemVenda itemVenda) {


        Produto produto = produtoRepository
                .findById(itemVenda.getProduto().getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Produto não encontrado."
                        ));


        // Busca o preço oficial do produto
       /* itemVenda.setValorUnitario(
                produto.getPrecoVenda()
        );
*/

        // Calcula subtotal
        itemVenda.setSubtotal(
                itemVenda.getQuantidade()
                        *
                        produto.getPrecoVenda()
        );


        return repository.save(itemVenda);

    }




    public ItemVenda atualizar(
            Long id,
            ItemVenda itemAtualizado) {


        ItemVenda item = buscarPorId(id);


        Produto produto = produtoRepository
                .findById(
                        itemAtualizado
                                .getProduto()
                                .getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Produto não encontrado."
                        ));



        item.setProduto(produto);


        item.setQuantidade(
                itemAtualizado.getQuantidade()
        );


        /*item.setValorUnitario(
                produto.getPrecoVenda()
        );*/


        item.setSubtotal(
                item.getQuantidade()
                        *
                        produto.getPrecoVenda()
        );


        return repository.save(item);

    }



    public void excluir(Long id) {

        repository.delete(
                buscarPorId(id)
        );

    }

}