package com.example.banco_api.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransacaoRequest {

    @NotNull(message = "A conta de origem é obrigatória")
    private Long contaOrigem;

    @NotNull(message = "A conta de destino é obrigatória")
    private Long contaDestino;

    @NotNull(message = "O valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor mínimo de transferência é 0.01")
    private BigDecimal valor;
}