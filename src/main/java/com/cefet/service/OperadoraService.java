package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Operadora;
import com.cefet.repository.OperadoraRepository;

// OperadoraService
@Service
public class OperadoraService {
    private final OperadoraRepository repo;

    public OperadoraService(OperadoraRepository repo) {
        this.repo = repo;
    }

    public List<Operadora> listarTodos() {
        return repo.findAll();
    }

    public Optional<Operadora> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Operadora salvar(Operadora operadora) {
        return repo.save(operadora);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
