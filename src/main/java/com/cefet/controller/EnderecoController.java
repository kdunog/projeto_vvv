package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Endereco;
import com.cefet.service.EnderecoService;

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

// EnderecoController
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    private final EnderecoService service;
    public EnderecoController(EnderecoService service) { this.service = service; }

    @GetMapping public List<Endereco> listarTodos() { return service.listarTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Endereco salvar(@RequestBody Endereco endereco) { return service.salvar(endereco); }
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody Endereco endereco) {
        if (!service.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        endereco.setId(id);
        return ResponseEntity.ok(service.salvar(endereco));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Endereco> atualizarParcial(@PathVariable Long id, @RequestBody Endereco enderecoAtualizado) {
        return service.buscarPorId(id)
                .map(endereco -> {
                    if (enderecoAtualizado.getCidade() != null) endereco.setCidade(enderecoAtualizado.getCidade());
                    if (enderecoAtualizado.getLogradouro() != null) endereco.setLogradouro(enderecoAtualizado.getLogradouro());
                    if (enderecoAtualizado.getNumero() != null) endereco.setNumero(enderecoAtualizado.getNumero());
                    if (enderecoAtualizado.getComplemento() != null) endereco.setComplemento(enderecoAtualizado.getComplemento());
                    if (enderecoAtualizado.getBairro() != null) endereco.setBairro(enderecoAtualizado.getBairro());
                    if (enderecoAtualizado.getCep() != null) endereco.setCep(enderecoAtualizado.getCep());
                    return ResponseEntity.ok(service.salvar(endereco));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id); return ResponseEntity.noContent().build();
    }
}
