package com.upoiny.kpmgv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VendaValorKpiResponse {

    private Double valorBrutoVendas;

    private Double valorDescontos;

    private Double valorLiquidoVendas;
}