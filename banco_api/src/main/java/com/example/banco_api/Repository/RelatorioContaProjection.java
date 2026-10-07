package com.example.banco_api.Repository;

import java.math.BigDecimal;

public interface RelatorioContaProjection {
    String getNomeCliente();
    String getCpf();
    String getNumeroConta();
    String getTipoConta();
    BigDecimal getSaldo();
}
