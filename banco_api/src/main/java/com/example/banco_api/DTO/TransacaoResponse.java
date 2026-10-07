package com.example.banco_api.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoResponse {

    private String mensagem;
    private Long contaOrigem;
    private Long contaDestino;
    private BigDecimal valor;
}
