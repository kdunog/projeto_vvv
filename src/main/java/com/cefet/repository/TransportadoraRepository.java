package com.cefet.repository;

import com.cefet.entity.Transportadora;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {
    Optional<Transportadora> findByName(String nome);
}
