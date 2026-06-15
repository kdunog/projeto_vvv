package com.cefet.service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Cidade;
import com.cefet.repository.CidadeRepository;

import jakarta.transaction.Transactional;

@Service
public class CidadeService {
    private final CidadeRepository repo;

    public CidadeService(CidadeRepository repo) {
        this.repo = repo;
    }

    public List<Cidade> listarTodos() {
        return repo.findAll();
    }

    public Optional<Cidade> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Cidade salvar(Cidade cidade) {
        if (cidade.getIndentificador() == null || !cidade.getIndentificador().matches("[A-Z]{3}")) {
            throw new RuntimeException("Identificador da cidade deve ter 3 letras maiúsculas.");
        }
        cidade.setNome(cidade.getNome().toUpperCase());
        return repo.save(cidade);
    }

    @Transactional
    public void deletar(Long id) {
        repo.deleteById(id);
    }

    public Cidade buscarPorNome(String nome) {
        return repo.findByNome(nome);
    }
}