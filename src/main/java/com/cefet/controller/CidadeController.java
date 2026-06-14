package com.cefet.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import com.cefet.entity.Cidade;
import com.cefet.service.CidadeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
    private final CidadeService service;
    public CidadeController(CidadeService service) { this.service = service; }

    @GetMapping public List<Cidade> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Cidade> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Cidade salvar(@RequestBody Cidade cidade) { return service.salvar(cidade); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
    @GetMapping("/nome/{nome}") public ResponseEntity<Cidade> buscarPorNome(@PathVariable String nome) {
        Cidade cidade = service.buscarPorNome(nome);
        return cidade != null ? ResponseEntity.ok(cidade) : ResponseEntity.notFound().build();
    }
}