package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Pagamento;
import com.cefet.service.PagamentoService;

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
@RequestMapping("/pagamentos")
public class PagamentoController {
    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pagamento> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Pagamento pagamento) {
        try {
            return ResponseEntity.ok(service.salvar(pagamento));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Pagamento pagamento) {
        try {
            if (!service.buscarPorId(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            pagamento.setId(id);
            return ResponseEntity.ok(service.salvar(pagamento));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Pagamento pagamentoAtualizado) {
        try {
            return service.buscarPorId(id)
                    .map(pagamento -> {
                        if (pagamentoAtualizado.getReserva() != null) pagamento.setReserva(pagamentoAtualizado.getReserva());
                        if (pagamentoAtualizado.getMetodoPagamento() != null) pagamento.setMetodoPagamento(pagamentoAtualizado.getMetodoPagamento());
                        if (pagamentoAtualizado.getNumeroDeParcelas() != null) pagamento.setNumeroDeParcelas(pagamentoAtualizado.getNumeroDeParcelas());
                        if (pagamentoAtualizado.getValor() != null) pagamento.setValor(pagamentoAtualizado.getValor());
                        if (pagamentoAtualizado.getStatus() != null) pagamento.setStatus(pagamentoAtualizado.getStatus());
                        return ResponseEntity.ok(service.salvar(pagamento));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
