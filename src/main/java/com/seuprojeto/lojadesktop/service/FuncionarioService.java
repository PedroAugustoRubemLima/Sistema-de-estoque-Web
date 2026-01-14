package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.model.Funcionario;
import com.seuprojeto.lojadesktop.repository.FuncionarioRepository;
import com.seuprojeto.lojadesktop.util.HashUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Funcionario salvar(Funcionario funcionario) {
        funcionario.setSenha(HashUtil.hashSenha(funcionario.getSenha()));
        funcionario.setAtivo(true);
        return repository.save(funcionario);
    }

    public List<Funcionario> listarAtivos() {
        return repository.findAll()
                .stream()
                .filter(Funcionario::getAtivo)
                .toList();
    }

    public Optional<Funcionario> autenticar(String usuario, String senha) {
        Optional<Funcionario> funcionario =
                repository.findByUsuarioAndAtivoTrue(usuario);

        if (funcionario.isPresent()) {
            String senhaHash = HashUtil.hashSenha(senha);
            if (senhaHash.equals(funcionario.get().getSenha())) {
                return funcionario;
            }
        }
        return Optional.empty();
    }

    public void inativar(Integer id) {
        Funcionario funcionario = repository.findById(id).orElseThrow();
        funcionario.setAtivo(false);
        repository.save(funcionario);
    }
}
