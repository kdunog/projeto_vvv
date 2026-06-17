package com.cefet.serviceTest;
import com.cefet.entity.Pagamento;
import com.cefet.entity.Reserva;
import com.cefet.entity.Ticket;
import com.cefet.repository.TicketRepository;
import com.cefet.service.ReservaService;
import com.cefet.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ReservaService reservaService;
    @InjectMocks
    private TicketService ticketService;
    @Test
    @DisplayName("Deve emitir ticket para reserva confirmada com pagamento")
    void deveEmitirTicketComSucesso() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus("CONFIRMADA");
        Pagamento pagamento = new Pagamento();
        pagamento.setValorFinal(new BigDecimal("200.00"));
        reserva.setPagamento(pagamento);
        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> 
invocation.getArgument(0));
        Ticket ticket = ticketService.emitirTicket(1L);
        assertNotNull(ticket);
        assertEquals(reserva, ticket.getReserva());
        assertEquals(new BigDecimal("200.00"), ticket.getValor());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
    @Test
    @DisplayName("Deve lançar exceção quando a reserva não estiver confirmada")
    void deveLancarExcecaoReservaNaoConfirmada() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus("PENDENTE");
        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
ticketService.emitirTicket(1L));
        assertEquals("Pagamento deve estar confirmado para emissão do ticket.", 
exception.getMessage());
        verify(ticketRepository, never()).save(any());
    }
    @Test
    @DisplayName("Deve lançar exceção quando a reserva não possuir pagamento")
    void deveLancarExcecaoSemPagamento() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus("CONFIRMADA");
        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
ticketService.emitirTicket(1L));
        assertEquals("Reserva não possui pagamento associado.", 
exception.getMessage());
        verify(ticketRepository, never()).save(any());
    }
    @Test
    @DisplayName("Deve lançar exceção quando a reserva já possuir ticket")
    void deveLancarExcecaoTicketJaEmitido() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus("CONFIRMADA");
        reserva.setPagamento(new Pagamento());
        reserva.setTicket(new Ticket());
        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
ticketService.emitirTicket(1L));
        assertEquals("Esta reserva já possui ticket emitido.", 
exception.getMessage());
        verify(ticketRepository, never()).save(any());
    }
}