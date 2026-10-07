package com.example.banco_api.Repository;

import com.example.banco_api.Entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumero(String numero);

    boolean existsByNumero(String numero);

    // Chama a Function do PostgreSQL que obtém o saldo
    @Query(value = "SELECT fn_consultar_saldo(:contaId)", nativeQuery = true)
    BigDecimal buscarSaldoViaFunction(@Param("contaId") Long contaId);

    // Chama a Stored Procedure do PostgreSQL para realizar a transferência
    @Procedure(procedureName = "sp_realizar_transferencia")
    void executarTransferencia(
            @Param("p_origem_id") Long origemId,
            @Param("p_destino_id") Long destinoId,
            @Param("p_valor") BigDecimal valor
    );

    // Consulta à View do PostgreSQL para emissão do relatório
    @Query(value = "SELECT nome_cliente AS nomeCliente, cpf, numero_conta AS numeroConta, " +
            "tipo_conta AS tipoConta, saldo FROM vw_relatorio_contas", nativeQuery = true)
    List<RelatorioContaProjection> buscarRelatorioContasView();
}
