package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Credito;
import com.cefet.repository.CreditoRepository;

// CreditoService
@Service
public class CreditoService {
    private final CreditoRepository repo;

    public CreditoService(CreditoRepository repo) {
        this.repo = repo;
    }

    public List<Credito> listarTodos() {
        return repo.findAll();
    }

    public Optional<Credito> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Credito salvar(Credito credito) {
        return repo.save(credito);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
