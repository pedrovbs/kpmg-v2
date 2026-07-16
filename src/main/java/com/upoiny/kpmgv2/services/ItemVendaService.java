package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.ItemVenda;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Venda;
import com.upoiny.kpmgv2.repositories.ItemVendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemVendaService {

    @Autowired
    private ItemVendaRepository repository;

    public Page<ItemVenda> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ItemVenda> pesquisarPorProduto(String search, Pageable pageable) {
        return repository.findByProdutoNomeContainingIgnoreCase(search, pageable);
    }

    public ItemVenda buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Item da venda não encontrado."));
    }

    public Page<ItemVenda> listarPorVenda(
            Venda venda,
            Pageable pageable) {

        return repository.findByVenda(venda, pageable);
    }

    public Page<ItemVenda> listarPorProduto(
            Produto produto,
            Pageable pageable) {

        return repository.findByProduto(produto, pageable);
    }

    public ItemVenda salvar(ItemVenda itemVenda) {

        itemVenda.setSubtotal(
                itemVenda.getQuantidade() * itemVenda.getValorUnitario()
        );

        return repository.save(itemVenda);
    }

    public ItemVenda atualizar(Long id, ItemVenda itemAtualizado) {

        ItemVenda item = buscarPorId(id);

        item.setVenda(itemAtualizado.getVenda());
        item.setProduto(itemAtualizado.getProduto());
        item.setQuantidade(itemAtualizado.getQuantidade());
        item.setValorUnitario(itemAtualizado.getValorUnitario());

        item.setSubtotal(
                itemAtualizado.getQuantidade() *
                        itemAtualizado.getValorUnitario()
        );

        return repository.save(item);
    }

    public void excluir(Long id) {

        repository.delete(buscarPorId(id));

    }

}
