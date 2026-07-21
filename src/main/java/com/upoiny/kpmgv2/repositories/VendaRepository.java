package com.upoiny.kpmgv2.repositories;

import com.upoiny.kpmgv2.dto.ClienteMaiorCompradorDTO;
import com.upoiny.kpmgv2.dto.ClienteVendaKpiResponse;
import com.upoiny.kpmgv2.dto.ProdutoVendaKpiResponse;
import com.upoiny.kpmgv2.dto.VendaValorKpiResponse;
import com.upoiny.kpmgv2.entities.Cliente;
import com.upoiny.kpmgv2.entities.Funcionario;
import com.upoiny.kpmgv2.entities.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("""
        SELECT new com.upoiny.kpmgv2.dto.ClienteMaiorCompradorDTO(
            v.cliente.id,
            v.cliente.nome,
            COUNT(v),
            SUM(v.valorFinal)
        )
        FROM Venda v
        GROUP BY v.cliente.id, v.cliente.nome
        ORDER BY COUNT(v) DESC
    """)
    Page<ClienteMaiorCompradorDTO> findClienteComMaisCompras(
            Pageable pageable
    );


    @Override
    @EntityGraph(attributePaths = {
            "cliente",
            "funcionario",
            "itens",
            "itens.produto"
    })
    Page<Venda> findAll(Pageable pageable);


    Page<Venda> findByCliente(
            Cliente cliente,
            Pageable pageable
    );


    Page<Venda> findByFuncionario(
            Funcionario funcionario,
            Pageable pageable
    );


    @EntityGraph(attributePaths = {
            "cliente",
            "funcionario",
            "itens",
            "itens.produto"
    })
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


    @Query("""
        SELECT new com.upoiny.kpmgv2.dto.ClienteVendaKpiResponse(
            c.id,
            c.nome,
            COUNT(v.id),
            SUM(v.valorFinal)
        )
        FROM Venda v
        JOIN v.cliente c
        GROUP BY c.id, c.nome
        ORDER BY SUM(v.valorFinal) DESC
    """)
    List<ClienteVendaKpiResponse> buscarClientesQueMaisCompraram(
            Pageable pageable
    );


    @Query("""
        SELECT new com.upoiny.kpmgv2.dto.VendaValorKpiResponse(
            COALESCE(SUM(v.valorTotal), 0.0),
            COALESCE(SUM(v.desconto), 0.0),
            COALESCE(SUM(v.valorFinal), 0.0)
        )
        FROM Venda v
    """)
    VendaValorKpiResponse buscarResumoFinanceiroVendas();


    @Query("""
        SELECT new com.upoiny.kpmgv2.dto.ProdutoVendaKpiResponse(
            p.id,
            p.nome,
            SUM(i.quantidade),
            SUM(i.subtotal)
        )
        FROM ItemVenda i
        JOIN i.produto p
        GROUP BY p.id, p.nome
        ORDER BY SUM(i.quantidade) DESC
    """)
    List<ProdutoVendaKpiResponse> buscarProdutosMaisVendidos(
            Pageable pageable
    );

}