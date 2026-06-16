package com.cefet.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.cefet.entity.Reserva;
import com.cefet.entity.Ticket;
import com.cefet.repository.TicketRepository;
import com.cefet.service.ReservaService;

import jakarta.transaction.Transactional;

@Service
public class TicketService {

    private final TicketRepository repo;
    private final ReservaService reservaService;

    public TicketService(TicketRepository repo, ReservaService reservaService) {
        this.repo = repo;
        this.reservaService = reservaService;
    }

    @Transactional
    public Ticket emitirTicket(Long reservaId) {
        Reserva reserva = reservaService.buscarPorId(reservaId);

        if (!"CONFIRMADA".equals(reserva.getStatus())) {
            throw new RuntimeException("Pagamento deve estar confirmado para emissão do ticket.");
        }

        if (reserva.getPagamento() == null) {
            throw new RuntimeException("Reserva não possui pagamento associado.");
        }

        if (reserva.getTicket() != null) {
            throw new RuntimeException("Esta reserva já possui ticket emitido.");
        }

        Ticket ticket = new Ticket();
        ticket.setReserva(reserva);
        ticket.setValor(reserva.getPagamento().getValorFinal());

        return repo.save(ticket);
    }

    public List<Ticket> listarTodos() {
        return repo.findAll();
    }

    public Optional<Ticket> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Ticket salvar(Ticket ticket) {
        return repo.save(ticket);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}