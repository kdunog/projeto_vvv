package com.cefet.serviceTest;

import com.cefet.entity.Pagamento;
import com.cefet.entity.Reserva;
import com.cefet.entity.Passageiro;
import com.cefet.repository.PagamentoRepository;
import com.cefet.repository.ReservaRepository;
import com.cefet.service.ReservaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import com.cefet.service.PagamentoService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)

class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService pagamentoService;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Test
    @DisplayName("Deve aplicar 40% de desconto infantil para passageiros entre 2 e 10 anos (RN004)")
    void deveAplicarDescontoInfantil() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        Passageiro passageiro = new Passageiro();
        passageiro.setIdade(5);
        reserva.setPassageiro(passageiro);

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setValor(BigDecimal.valueOf(100.0));

        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento processado = pagamentoService.salvar(pagamento);

        assertEquals(0, processado.getDesconto().compareTo(BigDecimal.valueOf(40.0)));
        assertEquals(0, processado.getJuros().compareTo(BigDecimal.ZERO));
        assertEquals(0, processado.getValorFinal().compareTo(BigDecimal.valueOf(60.0)));
        assertEquals("CONFIRMADO", processado.getStatus());
        assertEquals("CONFIRMADA", reserva.getStatus());
        assertEquals(Boolean.TRUE, reserva.getConfirmada());
    }

    @Test
    @DisplayName("Deve acrescer 5% de juros para parcelamentos acima de 3 vezes (RN009)")
    void deveAcrescerJurosNoParcelamentoAlto() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        Passageiro passageiro = new Passageiro();
        passageiro.setIdade(30);
        reserva.setPassageiro(passageiro);

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setNumeroDeParcelas(5);
        pagamento.setValor(BigDecimal.valueOf(100.0));

        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento processado = pagamentoService.salvar(pagamento);

        assertEquals(0, processado.getDesconto().compareTo(BigDecimal.ZERO));
        assertEquals(0, processado.getJuros().compareTo(BigDecimal.valueOf(5.0)));
        assertEquals(0, processado.getValorFinal().compareTo(BigDecimal.valueOf(105.0)));
        assertEquals("CONFIRMADO", processado.getStatus());
    }

    @Test
    @DisplayName("Deve confirmar a reserva ao salvar o pagamento")
    void deveAtualizarStatusParaVendaOnline() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        Passageiro passageiro = new Passageiro();
        passageiro.setIdade(25);
        reserva.setPassageiro(passageiro);

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setValor(BigDecimal.valueOf(200.0));

        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        pagamentoService.salvar(pagamento);

        assertEquals("CONFIRMADA", reserva.getStatus());
        assertEquals(Boolean.TRUE, reserva.getConfirmada());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagamento não possuir reserva")
    void deveLancarExcecaoSemReserva() {
        Pagamento pagamento = new Pagamento();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pagamentoService.salvar(pagamento));

        assertEquals("Pagamento deve possuir uma reserva com ID.", exception.getMessage());
        verify(pagamentoRepository, never()).save(any());
    }
}