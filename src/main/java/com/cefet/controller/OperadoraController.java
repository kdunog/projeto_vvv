package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Operadora;
import com.cefet.service.OperadoraService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// OperadoraController
@RestController
@RequestMapping("/operadoras")
public class OperadoraController {
    private final OperadoraService service;
    public OperadoraController(OperadoraService service) { this.service = service; }

    @GetMapping public List<Operadora> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Operadora> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Operadora salvar(@RequestBody Operadora operadora) { return service.salvar(operadora); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}
