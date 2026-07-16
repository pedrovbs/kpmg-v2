package com.upoiny.kpmgv2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="itens_compra")
@Getter @Setter
public class ItemCompra {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Compra compra;

    @ManyToOne
    private Produto produto;

    private Integer quantidade;

    private Double valorUnitario;

    private Double subtotal;

}