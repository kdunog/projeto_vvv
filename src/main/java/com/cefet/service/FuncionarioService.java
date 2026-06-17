package com.cefet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Funcionario;
import com.cefet.entity.PontoVenda;
import com.cefet.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repo;
    private final PontoVendaService pontoVendaService;

    public FuncionarioService(FuncionarioRepository repo, PontoVendaService pontoVendaService) {
        this.repo = repo;
        this.pontoVendaService = pontoVendaService;
    }

    @Transactional(readOnly = true)
    public List<Funcionario> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Funcionario> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {

        if (funcionario.getPontosVenda() == null || funcionario.getPontosVenda().isEmpty()) {
            throw new RuntimeException("Funcionário deve possuir pelo menos um ponto de venda.");
        }

        List<PontoVenda> pontosValidos = new ArrayList<>();
        for (PontoVenda ponto : funcionario.getPontosVenda()) {
            if (ponto == null || ponto.getId() == null) {
                throw new RuntimeException("Cada ponto de venda deve possuir um ID.");
            }
            PontoVenda pontoExistente = pontoVendaService.buscarPorId(ponto.getId())
                    .orElseThrow(() -> new RuntimeException("Ponto de venda não encontrado: " + ponto.getId()));
            pontosValidos.add(pontoExistente);
        }

        funcionario.setPontosVenda(pontosValidos);

        // RN011 - máximo de 2 pontos de venda
        if (funcionario.getPontosVenda().size() > 2) {
            throw new RuntimeException("Funcionário não pode estar vinculado a mais de dois pontos de venda.");
        }

        // RN012 - primeiro funcionário do ponto é gerente
        boolean gerente = false;
        for (PontoVenda ponto : funcionario.getPontosVenda()) {
            long quantidade = repo.contarFuncionariosDoPonto(ponto.getId());
            if (quantidade == 0) {
                gerente = true;
                break;
            }
        }
        funcionario.setCargo(gerente ? "GERENTE" : "VENDEDOR");

        return repo.save(funcionario);
    }

    @Transactional
    public void deletar(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Funcionario buscarPorCpf(String cpf) {
        return repo.findByCpf(cpf);
    }
}