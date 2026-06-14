package com.cefet.controller;
import com.cefet.entity.Banco;
import com.cefet.service.BancoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bancos")
public class BancoController {

    private final BancoService bancoService;

    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    @GetMapping
    public List<Banco> listarTodos() {
        return bancoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banco> buscarPorId(@PathVariable Long id) {
        return bancoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Banco banco) {
        try {
            return ResponseEntity.ok(bancoService.salvar(banco));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bancoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
