package com.example.banco_api.Controller;

import com.example.banco_api.DTO.TransacaoRequest;
import com.example.banco_api.DTO.TransacaoResponse;
import com.example.banco_api.Entity.Transacao;
import com.example.banco_api.Service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    // 3. Aciona a Procedure: POST /transacoes
    @PostMapping
    public ResponseEntity<TransacaoResponse> realizarTransacao(@RequestBody @Valid TransacaoRequest request) {
        TransacaoResponse response = transacaoService.realizarTransacao(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<Transacao>> listarPorConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(transacaoService.listarPorConta(contaId));
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listarTodas() {
        return ResponseEntity.ok(transacaoService.listarTodas());
    }

}

