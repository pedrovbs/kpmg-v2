package com.upoiny.kpmgv2.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProdutoCompraKpiResponse {

    private Long produtoId;

    private String produtoNome;

    private Long quantidadeComprada;

    private Double valorTotalComprado;
}