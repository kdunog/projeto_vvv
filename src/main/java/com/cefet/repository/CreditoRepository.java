package com.cefet.repository;

import com.cefet.entity.Credito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditoRepository extends JpaRepository<Credito, Long> {
}
