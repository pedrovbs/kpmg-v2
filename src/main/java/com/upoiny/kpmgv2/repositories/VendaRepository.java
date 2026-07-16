package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.entities.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Override
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Page<Venda> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Venda> findById(Long id);

    Page<Venda> findByCliente(
            Cliente cliente,
            Pageable pageable
    );

    Page<Venda> findByFuncionario(
            Funcionario funcionario,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Page<Venda> findByStatusContainingIgnoreCase(
            String status,
            Pageable pageable
    );

    Page<Venda> findByFormaPagamentoContainingIgnoreCase(
            String formaPagamento,
            Pageable pageable
    );

    Page<Venda> findByDataVendaBetween(
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    );

}
