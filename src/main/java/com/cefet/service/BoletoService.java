package com.cefet.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cefet.entity.Boleto;
import com.cefet.repository.BoletoRepository;

@Service
public class BoletoService {
    private final BoletoRepository repo;
    public BoletoService(BoletoRepository repo) { 
        this.repo = repo; }
    public List<Boleto> listarTodos() { 
        return repo.findAll(); }
    public Optional<Boleto> buscarPorId(Long id) { 
        return repo.findById(id); }
    public Boleto salvar(Boleto boleto) { 
        return repo.save(boleto); }
    public void deletar(Long id) { 
        repo.deleteById(id); }
}