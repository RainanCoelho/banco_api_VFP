package com.example.banco_api.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ContaRequest {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotBlank(message = "O número da conta é obrigatório")
    private String numero;

    @NotBlank(message = "O tipo da conta é obrigatório")
    @Pattern(regexp = "CORRENTE|POUPANCA", message = "O tipo deve ser CORRENTE ou POUPANCA")
    private String tipo;

    @NotNull(message = "O saldo inicial é obrigatório")
    @DecimalMin(value = "0.00", message = "O saldo inicial não pode ser negativo")
    private BigDecimal saldoInicial;
}