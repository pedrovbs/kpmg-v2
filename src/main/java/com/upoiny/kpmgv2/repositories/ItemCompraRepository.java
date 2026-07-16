package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.ItemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {

    Page<ItemCompra> findByCompraId(Long compraId, Pageable pageable);

    Page<ItemCompra> findByProdutoId(Long produtoId, Pageable pageable);

    Page<ItemCompra> findByProdutoNomeContainingIgnoreCase(String search, Pageable pageable);

}
