package com.cefet.repository;

import com.cefet.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    Cidade findByNome(String nome);
}
