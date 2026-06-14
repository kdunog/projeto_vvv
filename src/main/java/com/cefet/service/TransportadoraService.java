package com.cefet.service;

import com.cefet.entity.Transportadora;
import org.springframework.stereotype.Service;

import com.cefet.repository.TransportadoraRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransportadoraService {
    private final TransportadoraRepository transportadoraRepository;
    
    public TransportadoraService(TransportadoraRepository transportadoraRepository) {
        this.transportadoraRepository = transportadoraRepository;
    }

    public List<Transportadora> listarTodos() {
        return transportadoraRepository.findAll();
    }

    public Optional<Transportadora> buscarPorId(Long id) {
        return transportadoraRepository.findById(id);
    }

    public Transportadora salvar(Transportadora transportadora) {
        return transportadoraRepository.save(transportadora);
    }

    public void deletar(Long id) {
        transportadoraRepository.deleteById(id);
    }
}
