package com.upoiny.kpmgv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class VendaResponse {

    private Long id;

    private LocalDateTime dataVenda;

    private Double valorTotal;

    private Double desconto;

    private Double valorFinal;

    private String formaPagamento;

    private String status;

    private String observacoes;

    private Long clienteId;

    private String clienteNome;

    private Long funcionarioId;

    private String funcionarioNome;

    private List<ItemVendaResponse> itens;
}