package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Transportadora;
import com.cefet.service.TransportadoraService;

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
@RequestMapping("/transportadoras")
public class TransportadoraController {
    private final TransportadoraService service;

    public TransportadoraController(TransportadoraService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transportadora> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transportadora> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Transportadora salvar(@RequestBody Transportadora transportadora) {
        return service.salvar(transportadora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transportadora> atualizar(@PathVariable Long id, @RequestBody Transportadora transportadora) {
        if (!service.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        transportadora.setId(id);
        return ResponseEntity.ok(service.salvar(transportadora));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Transportadora> atualizarParcial(@PathVariable Long id, @RequestBody Transportadora transportadoraAtualizada) {
        return service.buscarPorId(id)
                .map(transportadora -> {
                    if (transportadoraAtualizada.getNome() != null) transportadora.setNome(transportadoraAtualizada.getNome());
                    if (transportadoraAtualizada.getCnpj() != null) transportadora.setCnpj(transportadoraAtualizada.getCnpj());
                    return ResponseEntity.ok(service.salvar(transportadora));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
