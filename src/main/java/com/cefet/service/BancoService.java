package com.cefet.service;

import com.cefet.entity.Banco;
import com.cefet.repository.BancoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BancoService {

    private final BancoRepository bancoRepository;

    public BancoService(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    public List<Banco> listarTodos() {
        return bancoRepository.findAll();
    }

    public Optional<Banco> buscarPorId(Long id) {
        return bancoRepository.findById(id);
    }

    public Banco salvar(Banco banco) {
        return bancoRepository.save(banco);
    }

    public void deletar(Long id) {
        bancoRepository.deleteById(id);
    }
}
