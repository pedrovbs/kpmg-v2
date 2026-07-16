package com.upoiny.kpmgv2.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
@Getter
@Setter
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataCompra;

    private Double valorTotal;

    private String status;

    private String observacoes;

    @ManyToOne
    private Fornecedor fornecedor;

    @ManyToOne
    private Funcionario funcionario;

    @OneToMany(
            mappedBy = "compra",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemCompra> itens = new ArrayList<>();

}