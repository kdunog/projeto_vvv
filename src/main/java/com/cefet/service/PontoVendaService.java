package com.cefet.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.cefet.entity.Endereco;
import com.cefet.entity.PontoVenda;
import com.cefet.repository.PontoVendaRepository;

@Service
public class PontoVendaService {

    private final PontoVendaRepository repo;
    private final EnderecoService enderecoService;

    public PontoVendaService(PontoVendaRepository repo, EnderecoService enderecoService) {
        this.repo = repo;
        this.enderecoService = enderecoService;
    }

    public List<PontoVenda> listarTodos() {
        return repo.findAll();
    }

    public Optional<PontoVenda> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public PontoVenda salvar(PontoVenda pontoVenda) {
        if (pontoVenda.getEndereco() == null || pontoVenda.getEndereco().getId() == null) {
            throw new RuntimeException("Ponto de venda deve possuir endereço válido.");
        }

        Endereco endereco = enderecoService.buscarPorId(pontoVenda.getEndereco().getId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado."));

        pontoVenda.setEndereco(endereco);
        return repo.save(pontoVenda);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
