package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Endereco;
import com.cefet.repository.EnderecoRepository;

// EnderecoService
@Service
public class EnderecoService {
    private final EnderecoRepository repo;

    public EnderecoService(EnderecoRepository repo) {
        this.repo = repo;
    }

    public List<Endereco> listarTodos() {
        return repo.findAll();
    }

    public Optional<Endereco> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Endereco salvar(Endereco endereco) {
        return repo.save(endereco);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
