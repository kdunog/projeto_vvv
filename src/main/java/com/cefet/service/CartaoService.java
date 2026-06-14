package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Cartao;
import com.cefet.repository.CartaoRepository;


@Service
public class CartaoService {
    private final CartaoRepository repo;

    public CartaoService(CartaoRepository repo) {
        this.repo = repo;
    }

    public List<Cartao> listarTodos() {
        return repo.findAll();
    }

    public Optional<Cartao> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Cartao salvar(Cartao cartao) {
        return repo.save(cartao);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}