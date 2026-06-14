package com.cefet.controller;
import java.util.List;
import com.cefet.entity.PontoVenda;
import com.cefet.service.PontoVendaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pontos-venda")
public class PontoVendaController {
    private final PontoVendaService service;

    public PontoVendaController(PontoVendaService service) {
        this.service = service;
    }

    @GetMapping
    public List<PontoVenda> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontoVenda> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PontoVenda salvar(@RequestBody PontoVenda pontoVenda) {
        return service.salvar(pontoVenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
