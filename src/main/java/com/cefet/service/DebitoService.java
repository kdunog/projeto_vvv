package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Debito;
import com.cefet.repository.DebitoRepository;

// DebitoService
@Service
public class DebitoService {
    private final DebitoRepository repo;

    public DebitoService(DebitoRepository repo) {
        this.repo = repo;
    }

    public List<Debito> listarTodos() {
        return repo.findAll();
    }

    public Optional<Debito> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Debito salvar(Debito debito) {
        return repo.save(debito);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
