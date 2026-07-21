package com.upoiny.kpmgv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "itens_venda")
@Getter
@Setter
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    private Double precoUnitario;

    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "venda_id")
    private Venda venda;
}