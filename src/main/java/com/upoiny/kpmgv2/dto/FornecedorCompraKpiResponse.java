package com.upoiny.kpmgv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FornecedorCompraKpiResponse {

    private Long fornecedorId;

    private String fornecedorNome;

    private Long quantidadeCompras;

    private Double valorTotalCompras;
}