package com.cefet.repository;

import com.cefet.entity.Debito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitoRepository extends JpaRepository<Debito, Long> {
}
