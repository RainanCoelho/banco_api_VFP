package com.example.banco_api.Service;

import com.example.banco_api.DTO.TransacaoResponse;
import com.example.banco_api.DTO.TransacaoRequest;
import com.example.banco_api.Entity.Transacao;
import com.example.banco_api.Repository.ContaRepository;
import com.example.banco_api.Repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    // 3. Uso real da PROCEDURE do PostgreSQL
    @Transactional
    public TransacaoResponse realizarTransacao(TransacaoRequest request) {
        // Dispara a Stored Procedure no PostgreSQL
        contaRepository.executarTransferencia(
                request.getContaOrigem(),
                request.getContaDestino(),
                request.getValor()
        );

        return new TransacaoResponse(
                "Transferência realizada com sucesso via Stored Procedure.",
                request.getContaOrigem(),
                request.getContaDestino(),
                request.getValor()
        );
    }

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    public List<Transacao> listarPorConta(Long contaId) {
        return transacaoRepository.findByContaIdOrderByCriadoEmDesc(contaId);
    }


}
