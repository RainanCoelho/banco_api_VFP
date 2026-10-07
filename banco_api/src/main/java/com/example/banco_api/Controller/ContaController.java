package com.example.banco_api.Controller;

import com.example.banco_api.DTO.ContaRequest;
import com.example.banco_api.DTO.ContaResponse;
import com.example.banco_api.DTO.SaldoResponse;
import com.example.banco_api.Repository.RelatorioContaProjection;
import com.example.banco_api.Service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @PostMapping
    public ResponseEntity<ContaResponse> criarConta(@RequestBody @Valid ContaRequest request) {
        ContaResponse response = contaService.criarConta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 1. Chama a Function: GET /contas/{id}/saldo
    @GetMapping("/{id}/saldo")
    public ResponseEntity<SaldoResponse> consultarSaldo(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.consultarSaldoViaFunction(id));
    }

    // 2. Chama a View: GET /contas/relatorio
    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioContaProjection>> obterRelatorioContas() {
        return ResponseEntity.ok(contaService.listarRelatorioContasView());
    }
}
