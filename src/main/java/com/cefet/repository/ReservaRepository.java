package com.cefet.repository;

import com.cefet.entity.Reserva;
import com.cefet.entity.Modal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
        SELECT r FROM Reserva r
        JOIN FETCH r.passageiro
        JOIN FETCH r.cidadeOrigem
        JOIN FETCH r.cidadeDestino
        JOIN FETCH r.modal
    """)
    List<Reserva> findAllCompleto();

    @Query("""
        SELECT r FROM Reserva r
        JOIN FETCH r.passageiro
        JOIN FETCH r.cidadeOrigem
        JOIN FETCH r.cidadeDestino
        JOIN FETCH r.modal
        WHERE r.id = :id
    """)
    Optional<Reserva> findByIdCompleto(Long id);


    long countByModal(Modal modal);
}
