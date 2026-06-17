package com.cefet.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cefet.entity.Pagamento;
import com.cefet.entity.Reserva;
import com.cefet.repository.PagamentoRepository;
import com.cefet.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repo;
    private final ReservaService reservaService;
    private final ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public List<Pagamento> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pagamento> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Pagamento salvar(Pagamento pagamento) {

        if (pagamento == null || pagamento.getReserva() == null || pagamento.getReserva().getId() == null) {
            throw new RuntimeException("Pagamento deve possuir uma reserva com ID.");
        }

        Reserva reserva = reservaService.buscarPorId(pagamento.getReserva().getId());
        pagamento.setReserva(reserva);

        if (pagamento.getValor() == null) {
            throw new RuntimeException("Pagamento deve possuir um valor.");
        }

        // RN004
        Integer idade = reserva.getPassageiro().getIdade();

        if (idade != null && idade >= 2 && idade <= 10) {
            pagamento.setDesconto(pagamento.getValor().multiply(BigDecimal.valueOf(0.40)));
        } else {
            pagamento.setDesconto(BigDecimal.ZERO);
        }

        // RN009
        if (pagamento.getNumeroDeParcelas() != null && pagamento.getNumeroDeParcelas() > 3) {
            pagamento.setJuros(pagamento.getValor().multiply(BigDecimal.valueOf(0.05)));
        } else {
            pagamento.setJuros(BigDecimal.ZERO);
        }

        pagamento.setValorFinal(pagamento.getValor().subtract(pagamento.getDesconto()).add(pagamento.getJuros()));

        // RN010 - pagamento autorizado imediatamente
        pagamento.setStatus("CONFIRMADO");
        pagamento.setConfirmadoPelaOperadora(true);
        pagamento.setDataConfirmacaoOperadora(LocalDateTime.now());

        reserva.setStatus("CONFIRMADA");
        reserva.setConfirmada(true);

        reservaRepository.save(reserva);
        return repo.save(pagamento);
    }

    // RN017
    @Transactional
    public Pagamento confirmarOperadora(Long id) {

        Pagamento pagamento = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pagamento não encontrado."));

        pagamento.setConfirmadoPelaOperadora(true);

        pagamento.setDataConfirmacaoOperadora(
                LocalDateTime.now());

        pagamento.getReserva()
                .setStatus("CONFIRMADA");

        pagamento.getReserva()
                .setConfirmada(true);

        return repo.save(pagamento);
    }

    @Transactional
    public void deletar(Long id) {
        repo.deleteById(id);
    }
}