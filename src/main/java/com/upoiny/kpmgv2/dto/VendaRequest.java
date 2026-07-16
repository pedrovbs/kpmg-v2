package com.upoiny.kpmgv2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VendaRequest {
    private Long clienteId;
    private Long funcionarioId;
    private String formaPagamento;
    private Double desconto;
    private String observacoes;
    private List<ItemVendaRequest> itens;
}
