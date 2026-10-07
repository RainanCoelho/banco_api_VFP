package com.example.banco_api.Service;

import com.example.banco_api.DTO.ClienteRequest;
import com.example.banco_api.DTO.ClienteResponse;
import com.example.banco_api.Entity.Cliente;
import com.example.banco_api.Repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse criarCliente(ClienteRequest request) {
        if (clienteRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF.");
        }
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este e-mail.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setCpf(request.getCpf());
        cliente.setEmail(request.getEmail());

        Cliente salvo = clienteRepository.save(cliente);
        return new ClienteResponse(salvo.getId(), salvo.getNome(), salvo.getCpf(), salvo.getEmail());
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(c -> new ClienteResponse(c.getId(), c.getNome(), c.getCpf(), c.getEmail()))
                .toList();
    }
}
