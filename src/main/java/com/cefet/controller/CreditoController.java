package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Credito;
import com.cefet.service.CreditoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creditos")
public class CreditoController {
    private final CreditoService service;
    public CreditoController(CreditoService service) { this.service = service; }

    @GetMapping public List<Credito> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Credito> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Credito salvar(@RequestBody Credito credito) { return service.salvar(credito); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}