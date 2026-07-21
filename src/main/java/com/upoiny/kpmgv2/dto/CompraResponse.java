package com.upoiny.kpmgv2.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompraResponse {

    private Long id;

    private LocalDateTime dataCompra;

    private Double valorTotal;

    private String status;

    private String observacoes;

    private Long fornecedorId;

    private String fornecedorNome;

    private Long funcionarioId;

    private String funcionarioNome;
}