package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.ItemVenda;
import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    Page<ItemVenda> findByVenda(
            Venda venda,
            Pageable pageable
    );

    Page<ItemVenda> findByProduto(
            Produto produto,
            Pageable pageable
    );

    Page<ItemVenda> findByProdutoNomeContainingIgnoreCase(String search, Pageable pageable);

}
