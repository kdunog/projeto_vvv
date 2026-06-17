package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Passageiro;
import com.cefet.service.PassageiroService;

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
@RequestMapping("/passageiros")
public class PassageiroController {
    private final PassageiroService service;

    public PassageiroController(PassageiroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Passageiro> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passageiro> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Passageiro salvar(@RequestBody Passageiro passageiro) {
        return service.salvar(passageiro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passageiro> atualizar(@PathVariable Long id, @RequestBody Passageiro passageiro) {
        if (!service.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        passageiro.setId(id);
        return ResponseEntity.ok(service.salvar(passageiro));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Passageiro> atualizarParcial(@PathVariable Long id, @RequestBody Passageiro dadosAtualizados) {
        return service.buscarPorId(id)
                .map(passageiro -> {
                    if (dadosAtualizados.getNome() != null) {
                        passageiro.setNome(dadosAtualizados.getNome());
                    }
                    if (dadosAtualizados.getCpf() != null) {
                        passageiro.setCpf(dadosAtualizados.getCpf());
                    }
                    if (dadosAtualizados.getTelefone() != null) {
                        passageiro.setTelefone(dadosAtualizados.getTelefone());
                    }
                    if (dadosAtualizados.getEmail() != null) {
                        passageiro.setEmail(dadosAtualizados.getEmail());
                    }
                    if (dadosAtualizados.getSenha() != null) {
                        passageiro.setSenha(dadosAtualizados.getSenha());
                    }
                    if (dadosAtualizados.getIdade() != null) {
                        passageiro.setIdade(dadosAtualizados.getIdade());
                    }
                    return ResponseEntity.ok(service.salvar(passageiro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Passageiro> buscarPorCpf(@PathVariable String cpf) {
        return service.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
