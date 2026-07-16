package com.upoiny.kpmgv2.services;

import com.upoiny.kpmgv2.entities.ItemCompra;
import com.upoiny.kpmgv2.repositories.ItemCompraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemCompraService {

    private final ItemCompraRepository repository;

    public ItemCompraService(ItemCompraRepository repository) {
        this.repository = repository;
    }

    public Page<ItemCompra> listar(String search, Long compraId, Long produtoId, Pageable pageable) {
        if (search != null && !search.isBlank()) return repository.findByProdutoNomeContainingIgnoreCase(search, pageable);
        if (compraId != null) return repository.findByCompraId(compraId, pageable);
        if (produtoId != null) return repository.findByProdutoId(produtoId, pageable);
        return repository.findAll(pageable);
    }

    public ItemCompra buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item de compra não encontrado."));
    }

    public ItemCompra salvar(ItemCompra item) {
        recalcularSubtotal(item);
        return repository.save(item);
    }

    public ItemCompra atualizar(Long id, ItemCompra atualizado) {
        ItemCompra item = buscarPorId(id);
        item.setCompra(atualizado.getCompra());
        item.setProduto(atualizado.getProduto());
        item.setQuantidade(atualizado.getQuantidade());
        item.setValorUnitario(atualizado.getValorUnitario());
        recalcularSubtotal(item);
        return repository.save(item);
    }

    public void excluir(Long id) {
        repository.delete(buscarPorId(id));
    }

    private void recalcularSubtotal(ItemCompra item) {
        if (item.getQuantidade() == null || item.getQuantidade() <= 0 || item.getValorUnitario() == null) {
            throw new RuntimeException("Quantidade e valor unitário devem ser válidos.");
        }
        item.setSubtotal(item.getQuantidade() * item.getValorUnitario());
    }
}
