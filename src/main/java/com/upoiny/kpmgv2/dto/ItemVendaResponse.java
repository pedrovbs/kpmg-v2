package com.upoiny.kpmgv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemVendaResponse {

    private Long id;

    private Long produtoId;

    private String produtoNome;

    private Integer quantidade;

    private Double precoUnitario;

    private Double subtotal;
}