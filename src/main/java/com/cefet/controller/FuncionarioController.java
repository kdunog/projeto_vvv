package com.cefet.controller;
import java.util.List;
import com.cefet.entity.Funcionario;
import com.cefet.service.FuncionarioService;

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
@RequestMapping("/funcionarios")
public class FuncionarioController {
    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Funcionario> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Funcionario funcionario) {
        try {
            return ResponseEntity.ok(service.salvar(funcionario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Funcionario funcionario) {
        try {
            if (!service.buscarPorId(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            funcionario.setId(id);
            return ResponseEntity.ok(service.salvar(funcionario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Funcionario funcionarioAtualizado) {
        try {
            return service.buscarPorId(id)
                    .map(funcionario -> {
                        if (funcionarioAtualizado.getNome() != null) funcionario.setNome(funcionarioAtualizado.getNome());
                        if (funcionarioAtualizado.getCpf() != null) funcionario.setCpf(funcionarioAtualizado.getCpf());
                        if (funcionarioAtualizado.getTelefone() != null) funcionario.setTelefone(funcionarioAtualizado.getTelefone());
                        if (funcionarioAtualizado.getEmail() != null) funcionario.setEmail(funcionarioAtualizado.getEmail());
                        if (funcionarioAtualizado.getSenha() != null) funcionario.setSenha(funcionarioAtualizado.getSenha());
                        if (funcionarioAtualizado.getCargo() != null) funcionario.setCargo(funcionarioAtualizado.getCargo());
                        if (funcionarioAtualizado.getEnderecoResidencia() != null) funcionario.setEnderecoResidencia(funcionarioAtualizado.getEnderecoResidencia());
                        if (funcionarioAtualizado.getAutorizadoMultiplosPontos() != null) funcionario.setAutorizadoMultiplosPontos(funcionarioAtualizado.getAutorizadoMultiplosPontos());
                        return ResponseEntity.ok(service.salvar(funcionario));
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

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Funcionario> buscarPorCpf(@PathVariable String cpf) {
        Funcionario f = service.buscarPorCpf(cpf);
        return f != null ? ResponseEntity.ok(f) : ResponseEntity.notFound().build();
    }
}

