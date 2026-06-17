package com.cefet.service;

import com.cefet.entity.Reserva;
import com.cefet.entity.Transferencia;
import com.cefet.repository.TransferenciaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;

    @Transactional
    public Transferencia transferirReservaOnline(
            Reserva reserva) {

        if (true) {

            throw new RuntimeException(
                    "Apenas vendas online podem ser transferidas.");
        }

        Transferencia transferencia =
                new Transferencia();

        transferencia.setReserva(reserva);

        transferencia.setTransportadora(
                reserva.getModal()
                        .getTransportadora());

        transferencia.setDataTransferencia(
                LocalDateTime.now());

        return transferenciaRepository.save(
                transferencia);
    }
}