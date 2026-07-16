package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Produto;
import com.upoiny.kpmgv2.entities.Categoria;
import com.upoiny.kpmgv2.entities.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

    Page<Produto> findByCategoria(
            Categoria categoria,
            Pageable pageable
    );

    Page<Produto> findByFornecedor(
            Fornecedor fornecedor,
            Pageable pageable
    );

    Page<Produto> findByEstoqueLessThanEqual(
            Integer estoque,
            Pageable pageable
    );

}