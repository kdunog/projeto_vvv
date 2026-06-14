package com.cefet.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.cefet.entity.Funcionario;
import com.cefet.entity.PontoVenda;
import com.cefet.repository.PontoVendaRepository;

@Service
public class PontoVendaService {

    private final PontoVendaRepository repo;

    public PontoVendaService(PontoVendaRepository repo) {
        this.repo = repo;
    }

    public List<PontoVenda> listarTodos() {
        return repo.findAll();
    }

    public Optional<PontoVenda> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public PontoVenda salvar(PontoVenda pontoVenda) {

        boolean possuiGerente = false;

        if (pontoVenda.getFuncionarios() != null) {

            for (Funcionario funcionario :
                    pontoVenda.getFuncionarios()) {

                if ("GERENTE".equalsIgnoreCase(
                        funcionario.getCargo())) {

                    possuiGerente = true;
                    break;
                }
            }
        }

        if (!possuiGerente) {

            throw new RuntimeException(
                    "Todo ponto de venda deve possuir um gerente."
            );
        }

        return repo.save(pontoVenda);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}