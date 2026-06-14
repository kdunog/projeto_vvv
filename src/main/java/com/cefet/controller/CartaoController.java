package com.cefet.controller;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.entity.Cartao;
import com.cefet.service.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    private final CartaoService service;
    public CartaoController(CartaoService service) { this.service = service; }

    @GetMapping public List<Cartao> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Cartao> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Cartao salvar(@RequestBody Cartao cartao) { return service.salvar(cartao); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}