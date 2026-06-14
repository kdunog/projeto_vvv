package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Passageiro;
import com.cefet.repository.PassageiroRepository;

// PassageiroService
@Service
public class PassageiroService {
    private final PassageiroRepository repo;

    public PassageiroService(PassageiroRepository repo) {
        this.repo = repo;
    }

    public List<Passageiro> listarTodos() {
        return repo.findAll();
    }

    public Optional<Passageiro> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Passageiro salvar(Passageiro passageiro) {
        return repo.save(passageiro);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }

    public Optional<Passageiro> buscarPorCpf(String cpf) {
        return repo.findByCpf(cpf);
    }
}
