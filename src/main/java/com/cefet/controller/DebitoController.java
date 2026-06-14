package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Debito;
import com.cefet.service.DebitoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// DebitoController
@RestController
@RequestMapping("/debitos")
public class DebitoController {
    private final DebitoService service;
    public DebitoController(DebitoService service) { this.service = service; }

    @GetMapping public List<Debito> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Debito> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Debito salvar(@RequestBody Debito debito) { return service.salvar(debito); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}
