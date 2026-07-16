package com.upoiny.kpmgv2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVendaRequest {
    private Long produtoId;
    private Integer quantidade;
}
