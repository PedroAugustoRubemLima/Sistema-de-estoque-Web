package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Cliente;
import com.seuprojeto.lojadesktop.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente salvar(Cliente cliente) {
        cliente.setAtivo(true);
        return repository.save(cliente);
    }

    public List<Cliente> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public void inativar(Integer id) {
        Cliente cliente = repository.findById(id).orElseThrow();
        cliente.setAtivo(false);
        repository.save(cliente);
    }
}

