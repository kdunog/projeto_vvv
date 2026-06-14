package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Boleto;
import com.cefet.service.BoletoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boletos")
public class BoletoController {
    private final BoletoService service;
    public BoletoController(BoletoService service) { this.service = service; }

    @GetMapping public List<Boleto> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Boleto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Boleto salvar(@RequestBody Boleto boleto) { return service.salvar(boleto); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}
