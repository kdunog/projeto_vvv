package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Modal;
import com.cefet.service.ModalService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// ModalController
@RestController
@RequestMapping("/modais")
public class ModalController {
    private final ModalService service;
    public ModalController(ModalService service) { this.service = service; }

    @GetMapping public List<Modal> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Modal> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Modal salvar(@RequestBody Modal modal) { return service.salvar(modal); }
    @PutMapping("/{id}")
    public ResponseEntity<Modal> atualizar(@PathVariable Long id, @RequestBody Modal modal) {
        if (!service.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        modal.setId(id);
        return ResponseEntity.ok(service.salvar(modal));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Modal> atualizarParcial(@PathVariable Long id, @RequestBody Modal dadosAtualizados) {
        return service.buscarPorId(id)
                .map(modal -> {
                    if (dadosAtualizados.getTransportadora() != null) {
                        modal.setTransportadora(dadosAtualizados.getTransportadora());
                    }
                    if (dadosAtualizados.getTipo() != null) {
                        modal.setTipo(dadosAtualizados.getTipo());
                    }
                    if (dadosAtualizados.getCapacidade() != null) {
                        modal.setCapacidade(dadosAtualizados.getCapacidade());
                    }
                    if (dadosAtualizados.getUltimaManutencao() != null) {
                        modal.setUltimaManutencao(dadosAtualizados.getUltimaManutencao());
                    }
                    modal.setEmManutencao(dadosAtualizados.isEmManutencao());
                    return ResponseEntity.ok(service.salvar(modal));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}
