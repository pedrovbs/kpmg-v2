package com.upoiny.kpmgv2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="vendas")
@Getter
@Setter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataVenda;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private Double desconto;

    @Column(nullable = false)
    private Double valorFinal;

    @Column(nullable = false)
    private String formaPagamento;

    @Column(nullable = false)
    private String status;

    private String observacoes;

    // ============================
    // Relacionamentos
    // ============================

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @OneToMany(
            mappedBy = "venda",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ItemVenda> itens = new ArrayList<>();


}