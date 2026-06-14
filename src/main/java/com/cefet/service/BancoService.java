package com.cefet.service;

import com.cefet.entity.Banco;
import com.cefet.repository.BancoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BancoService {
    private final BancoRepository repo;
    public BancoService(BancoRepository repo) {
         this.repo = repo; }
    public List<Banco> listarTodos() { 
        return repo.findAll(); }
    public Optional<Banco> buscarPorId(Long id) { 
        return repo.findById(id); }
    public Banco salvar(Banco banco) { 
        return repo.save(banco); }
    public void deletar(Long id) { 
        repo.deleteById(id); }
    public Banco buscarPorNome(String nome) { 
        return repo.findByNome(nome); }
}
