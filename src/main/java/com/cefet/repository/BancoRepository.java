package com.cefet.repository;

import com.cefet.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long> {
    
    // Exemplo de método customizado:
    Banco findByNome(String nome);
}

