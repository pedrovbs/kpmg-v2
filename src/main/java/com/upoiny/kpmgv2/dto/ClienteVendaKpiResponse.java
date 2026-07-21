package com.upoiny.kpmgv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClienteVendaKpiResponse {

    private Long clienteId;

    private String clienteNome;

    private Long quantidadeCompras;

    private Double valorTotalComprado;
}