package com.cefet.controller;
import java.util.List;
import com.cefet.entity.PontoVenda;
import com.cefet.service.PontoVendaService;

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

    @PutMapping("/{id}")
    public ResponseEntity<PontoVenda> atualizar(@PathVariable Long id, @RequestBody PontoVenda pontoVenda) {
        if (!service.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        pontoVenda.setId(id);
        return ResponseEntity.ok(service.salvar(pontoVenda));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PontoVenda> atualizarParcial(@PathVariable Long id, @RequestBody PontoVenda pontoVendaAtualizado) {
        return service.buscarPorId(id)
                .map(pontoVenda -> {
                    if (pontoVendaAtualizado.getEndereco() != null) pontoVenda.setEndereco(pontoVendaAtualizado.getEndereco());
                    if (pontoVendaAtualizado.getTelefone() != null) pontoVenda.setTelefone(pontoVendaAtualizado.getTelefone());
                    if (pontoVendaAtualizado.getCnpj() != null) pontoVenda.setCnpj(pontoVendaAtualizado.getCnpj());
                    return ResponseEntity.ok(service.salvar(pontoVenda));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
