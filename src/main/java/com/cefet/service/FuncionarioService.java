package com.cefet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.cefet.entity.Funcionario;
import com.cefet.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repo;

    public FuncionarioService(FuncionarioRepository repo) {
        this.repo = repo;
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

        if (funcionario.getPontosVenda() == null ||
            funcionario.getPontosVenda().isEmpty()) {

            throw new RuntimeException(
                "Funcionário deve possuir pelo menos um ponto de venda."
            );
        }

        if (funcionario.getPontosVenda().size() > 2) {

            throw new RuntimeException(
                "Funcionário não pode estar vinculado a mais de dois pontos de venda."
            );
        }

        if (funcionario.getPontosVenda().size() > 1 &&
            (funcionario.getAutorizadoMultiplosPontos() == null ||
             !funcionario.getAutorizadoMultiplosPontos())) {

            throw new RuntimeException(
                "Funcionário precisa de autorização do gerente para trabalhar em múltiplos pontos."
            );
        }

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