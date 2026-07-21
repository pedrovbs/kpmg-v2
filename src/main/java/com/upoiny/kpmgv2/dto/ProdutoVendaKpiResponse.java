package com.upoiny.kpmgv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProdutoVendaKpiResponse {

    private Long produtoId;

    private String produtoNome;

    private Long quantidadeVendida;

    private Double valorTotalVendido;
}