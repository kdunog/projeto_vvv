package com.cefet.serviceTest;

import com.cefet.entity.Reserva;
import com.cefet.repository.TransferenciaRepository;
import com.cefet.service.TransferenciaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    @DisplayName("Deve lançar exceção ao tentar transferir uma reserva")
    void deveLancarExcecaoTransferenciaInvalida() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transferenciaService.transferirReservaOnline(reserva));

        assertEquals("Apenas vendas online podem ser transferidas.", exception.getMessage());
        verify(transferenciaRepository, never()).save(any());
    }
}