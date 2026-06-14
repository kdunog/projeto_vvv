package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Pix;
import com.cefet.repository.PixRepository;

// PixService
@Service
public class PixService {
    private final PixRepository repo;

    public PixService(PixRepository repo) {
        this.repo = repo;
    }

    public List<Pix> listarTodos() {
        return repo.findAll();
    }

    public Optional<Pix> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Pix salvar(Pix pix) {
        return repo.save(pix);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}