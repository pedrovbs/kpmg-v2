package com.upoiny.kpmgv2.dto;

public record ClienteMaiorCompradorDTO(
    Long id,
    String nome,
    Long quantidadeCompras,
    Double valorTotalGasto
) {}