package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Compra;
import com.upoiny.kpmgv2.entities.Fornecedor;
import com.upoiny.kpmgv2.entities.Funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDateTime;
import java.util.Optional;


public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Override
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Page<Compra> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Compra> findById(Long id);


    /**
     * Buscar compras realizadas por fornecedor
     */
    Page<Compra> findByFornecedor(
            Fornecedor fornecedor,
            Pageable pageable
    );


    /**
     * Buscar compras realizadas por funcionário responsável
     */
    Page<Compra> findByFuncionario(
            Funcionario funcionario,
            Pageable pageable
    );


    /**
     * Buscar compras pelo status
     *
     * Exemplos:
     * FINALIZADA
     * CANCELADA
     */
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Page<Compra> findByStatusContainingIgnoreCase(
            String status,
            Pageable pageable
    );


    /**
     * Buscar compras em determinado período
     */
    Page<Compra> findByDataCompraBetween(
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    );


}
