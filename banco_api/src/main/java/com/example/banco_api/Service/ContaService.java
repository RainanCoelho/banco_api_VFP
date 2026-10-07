package com.example.banco_api.Service;

import com.example.banco_api.DTO.ContaRequest;
import com.example.banco_api.DTO.ContaResponse;
import com.example.banco_api.DTO.SaldoResponse;
import com.example.banco_api.Entity.Cliente;
import com.example.banco_api.Entity.Conta;
import com.example.banco_api.Repository.ClienteRepository;
import com.example.banco_api.Repository.ContaRepository;
import com.example.banco_api.Repository.RelatorioContaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public ContaResponse criarConta(ContaRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + request.getClienteId()));

        if (contaRepository.existsByNumero(request.getNumero())) {
            throw new IllegalArgumentException("Já existe uma conta com o número: " + request.getNumero());
        }

        Conta conta = new Conta();
        conta.setCliente(cliente);
        conta.setNumero(request.getNumero());
        conta.setTipo(request.getTipo());
        conta.setSaldo(request.getSaldoInicial());

        Conta salva = contaRepository.save(conta);
        return new ContaResponse(salva.getId(), salva.getCliente().getId(), salva.getNumero(), salva.getTipo(), salva.getSaldo());
    }

    // 1. Uso real da FUNCTION do PostgreSQL
    @Transactional(readOnly = true)
    public SaldoResponse consultarSaldoViaFunction(Long contaId) {
        if (!contaRepository.existsById(contaId)) {
            throw new IllegalArgumentException("Conta não encontrada com o ID: " + contaId);
        }
        BigDecimal saldo = contaRepository.buscarSaldoViaFunction(contaId);
        return new SaldoResponse(contaId, saldo);
    }

    // 2. Uso real da VIEW do PostgreSQL
    @Transactional(readOnly = true)
    public List<RelatorioContaProjection> listarRelatorioContasView() {
        return contaRepository.buscarRelatorioContasView();
    }
}
