package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Modal;
import com.cefet.service.ModalService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}
