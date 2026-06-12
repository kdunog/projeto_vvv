package com.cefet.repository;

import com.cefet.entity.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {
    Optional<Passageiro> findByCpf(String cpf);
}
