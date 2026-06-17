package com.cefet.serviceTest;

import com.cefet.entity.Pagamento;
import com.cefet.entity.Reserva;
import com.cefet.entity.Passageiro;
import com.cefet.entity.Credito;
import com.cefet.repository.PagamentoRepository;
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

    @Test
    @DisplayName("Deve aplicar 40% de desconto infantil para passageiros entre 3 e 9 anos (RN004)")
    void deveAplicarDescontoInfantil() {
        Reserva reserva = new Reserva();
        Passageiro passageiro = new Passageiro();
        passageiro.setIdade(5);
        reserva.setPassageiro(passageiro);
        reserva.setVendaOnline(false);

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setValor(BigDecimal.valueOf(100.0));

        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento processado = pagamentoService.salvar(pagamento);

        assertEquals(0, processado.getDesconto().compareTo(BigDecimal.valueOf(40.0)));
        assertEquals(0, processado.getJuros().compareTo(BigDecimal.ZERO));
        assertEquals(0, processado.getValorFinal().compareTo(BigDecimal.valueOf(60.0)));
        assertEquals("CONFIRMADO", processado.getStatus());
        assertEquals("AGUARDANDO_FUNCIONARIO", reserva.getStatus());
    }

    @Test
    @DisplayName("Deve acrescer 5% de juros para parcelamentos no crédito acima de 4 vezes (RN009)")
    void deveAcrescerJurosNoParcelamentoAlto() {
        Reserva reserva = new Reserva();
        Passageiro passageiro = new Passageiro();
        passageiro.setIdade(30);
        reserva.setPassageiro(passageiro);
        reserva.setVendaOnline(false);

        Credito credito = new Credito();
        credito.setNumeroDeParcelas(5);

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setCredito(credito);
        pagamento.setValor(BigDecimal.valueOf(100.0));

        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento processado = pagamentoService.salvar(pagamento);

        assertEquals(0, processado.getDesconto().compareTo(BigDecimal.ZERO));
        assertEquals(0, processado.getJuros().compareTo(BigDecimal.valueOf(5.0)));
        assertEquals(0, processado.getValorFinal().compareTo(BigDecimal.valueOf(105.0)));
        assertEquals("CONFIRMADO", processado.getStatus());
    }

    @Test
    @DisplayName("Deve alterar status da reserva para AGUARDANDO_OPERADORA se a venda for Online (RN015/RN017)")
    void deveAtualizarStatusParaVendaOnline() {
        Reserva reserva = new Reserva();
        Passageiro passageiro = new Passageiro();
        passageiro.setIdade(25);
        reserva.setPassageiro(passageiro);
        reserva.setVendaOnline(true);

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setValor(BigDecimal.valueOf(200.0));

        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        pagamentoService.salvar(pagamento);

        assertEquals("AGUARDANDO_OPERADORA", reserva.getStatus());
        assertEquals(Boolean.FALSE, reserva.getConfirmada());
    }
}