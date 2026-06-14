package com.cefet.repository;

import com.cefet.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaRepository
        extends JpaRepository<Transferencia, Long> {
}