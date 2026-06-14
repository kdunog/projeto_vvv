package com.cefet.service;

import com.cefet.entity.Pagamento;
import com.cefet.entity.Reserva;
import com.cefet.entity.Credito;
import com.cefet.repository.PagamentoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repo;

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

        Reserva reserva = pagamento.getReserva();

        if (reserva == null) {
            throw new RuntimeException(
                    "Pagamento deve possuir uma reserva."
            );
        }

        // RN004: desconto infantil
        Integer idade = reserva.getPassageiro().getIdade();

        if (idade != null && idade > 2 && idade < 10) {

            pagamento.setDesconto(
                    pagamento.getValor()
                            .multiply(BigDecimal.valueOf(0.40))
            );

        } else {

            pagamento.setDesconto(BigDecimal.ZERO);
        }

        // RN009: parcelamento com juros
        Credito credito = pagamento.getCredito();

        if (credito != null &&
            credito.getNumeroDeParcelas() > 4) {

            pagamento.setJuros(
                    pagamento.getValor()
                            .multiply(BigDecimal.valueOf(0.05))
            );

        } else {

            pagamento.setJuros(BigDecimal.ZERO);
        }

        // cálculo do valor final
        pagamento.setValorFinal(

                pagamento.getValor()
                        .subtract(pagamento.getDesconto())
                        .add(pagamento.getJuros())
        );

        // RN010: pagamento aprovado
        pagamento.setStatus("CONFIRMADO");

        // reserva confirmada após pagamento
        if (reserva.isVendaOnline()) {

        reserva.setStatus(
                "AGUARDANDO_OPERADORA");

        reserva.setConfirmada(false);

        } else {

        reserva.setStatus(
                "AGUARDANDO_FUNCIONARIO");

        reserva.setConfirmada(false);
        }   


        return repo.save(pagamento);
    }

    @Transactional
    public void deletar(Long id) {
        repo.deleteById(id);
    }

    @Transactional
    public Pagamento confirmarOperadora(Long id) {

        Pagamento pagamento = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pagamento não encontrado"));

        pagamento.setConfirmadoPelaOperadora(true);

        pagamento.setDataConfirmacaoOperadora(
                java.time.LocalDateTime.now());

        pagamento.getReserva()
                .setStatus("CONFIRMADA");

        pagamento.getReserva()
                .setConfirmada(true);

        return repo.save(pagamento);
    }
}