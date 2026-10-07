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
public class ContaResponse {

    private Long id;
    private Long clienteId;
    private String numero;
    private String tipo;
    private BigDecimal saldo;
}
