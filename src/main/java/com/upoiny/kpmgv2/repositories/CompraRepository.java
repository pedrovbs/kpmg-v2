package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.dto.FornecedorCompraKpiResponse;
import com.upoiny.kpmgv2.dto.ProdutoCompraKpiResponse;
import com.upoiny.kpmgv2.entities.Compra;
import com.upoiny.kpmgv2.entities.Fornecedor;
import com.upoiny.kpmgv2.entities.Funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CompraRepository extends JpaRepository<Compra, Long> {


    @Override
    @EntityGraph(attributePaths = {
            "fornecedor",
            "funcionario",
            "itens",
            "itens.produto"
    })
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
    @EntityGraph(attributePaths = {
            "fornecedor",
            "funcionario",
            "itens",
            "itens.produto"
    })
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

    @Query("""
    SELECT COUNT(c)
    FROM Compra c
""")
    Long contarTotalCompras();

    @Query("""
    SELECT new com.upoiny.kpmgv2.dto.FornecedorCompraKpiResponse(
        f.id,
        f.nomeFantasia,
        COUNT(c.id),
        COALESCE(SUM(c.valorTotal), 0.0)
    )
    FROM Compra c
    JOIN c.fornecedor f
    GROUP BY f.id, f.nomeFantasia
    ORDER BY COUNT(c.id) DESC
""")
    List<FornecedorCompraKpiResponse> buscarFornecedoresComMaisCompras();
    @Query("""
    SELECT new com.upoiny.kpmgv2.dto.ProdutoCompraKpiResponse(
        p.id,
        p.nome,
        SUM(i.quantidade),
        SUM(i.subtotal)
    )
    FROM ItemCompra i
    JOIN i.produto p
    GROUP BY p.id, p.nome
    ORDER BY SUM(i.quantidade) DESC
""")
    List<ProdutoCompraKpiResponse> buscarProdutosMaisComprados(Pageable pageable);

}
