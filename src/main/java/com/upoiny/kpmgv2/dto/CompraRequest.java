package com.upoiny.kpmgv2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompraRequest {

    private Long fornecedorId;

    private Long funcionarioId;

    private String observacoes;

    private List<ItemCompraRequest> itens;

}