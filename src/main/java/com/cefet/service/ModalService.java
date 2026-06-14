package com.cefet.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Modal;
import com.cefet.repository.ModalRepository;

import jakarta.transaction.Transactional;

@Service
public class ModalService {

    private final ModalRepository repo;

    public ModalService(ModalRepository repo) {
        this.repo = repo;
    }

    public List<Modal> listarTodos() {
        return repo.findAll();
    }

    public Optional<Modal> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Modal salvar(Modal modal) {

        if (modal.getUltimaManutencao() != null &&
        modal.getUltimaManutencao().isAfter(LocalDate.now())) {
        modal.setStatus("EM_MANUTENCAO");
        } else {
        modal.setStatus("DISPONIVEL");
        }   


        return repo.save(modal);
    }

    @Transactional
    public void deletar(Long id) {
        repo.deleteById(id);
    }
}