package com.cefet.serviceTest;

import com.cefet.entity.Cidade;
import com.cefet.entity.Modal;
import com.cefet.entity.Passageiro;
import com.cefet.entity.Reserva;
import com.cefet.repository.ReservaRepository;
import com.cefet.service.CidadeService;
import com.cefet.service.ModalService;
import com.cefet.service.PassageiroService;
import com.cefet.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private PassageiroService passageiroService;

    @Mock
    private CidadeService cidadeService;

    @Mock
    private ModalService modalService;

    @InjectMocks
    private ReservaService reservaService;

    private Passageiro passageiro;
    private Cidade origem;
    private Cidade destino;
    private Modal modal;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        passageiro = new Passageiro();
        passageiro.setId(1L);
        passageiro.setNome("João Silva");
        passageiro.setIdade(30);

        origem = new Cidade();
        origem.setId(1L);
        origem.setIndentificador("SPO");

        destino = new Cidade();
        destino.setId(2L);
        destino.setIndentificador("RJO");

        modal = new Modal();
        modal.setId(1L);
        modal.setCapacidade(50);
        modal.setEmManutencao(false);
        modal.setStatus("DISPONIVEL");

        reserva = new Reserva();
        reserva.setPassageiro(passageiro);
        reserva.setCidadeOrigem(origem);
        reserva.setCidadeDestino(destino);
        reserva.setModal(modal);
        reserva.setDataReserva(LocalDate.now().plusDays(1));
    }

    @Test
    @DisplayName("Deve listar reservas completas")
    void deveListarReservas() {
        when(reservaRepository.findAllCompleto()).thenReturn(List.of(new Reserva(), new Reserva()));

        assertEquals(2, reservaService.listarTodos().size());
    }

    @Test
    @DisplayName("Deve buscar reserva completa por id")
    void deveBuscarPorIdReservaCompleta() {
        reserva.setId(1L);
        when(reservaRepository.findByIdCompleto(1L)).thenReturn(Optional.of(reserva));

        Reserva resultado = reservaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a reserva não existir")
    void deveLancarExcecaoReservaNaoEncontrada() {
        when(reservaRepository.findByIdCompleto(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservaService.buscarPorId(99L));

        assertEquals("Reserva não encontrada!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve salvar reserva válida com status PENDENTE")
    void deveSalvarReservaValida() {
        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(cidadeService.buscarPorId(1L)).thenReturn(Optional.of(origem));
        when(cidadeService.buscarPorId(2L)).thenReturn(Optional.of(destino));
        when(modalService.buscarPorId(1L)).thenReturn(Optional.of(modal));
        when(reservaRepository.countByModal(modal)).thenReturn(0L);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva resultado = reservaService.salvar(reserva);

        assertEquals("PENDENTE", resultado.getStatus());
        assertFalse(resultado.getConfirmada());
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o modal estiver em manutenção")
    void deveLancarExcecaoModalEmManutencao() {
        modal.setEmManutencao(true);

        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(cidadeService.buscarPorId(1L)).thenReturn(Optional.of(origem));
        when(cidadeService.buscarPorId(2L)).thenReturn(Optional.of(destino));
        when(modalService.buscarPorId(1L)).thenReturn(Optional.of(modal));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservaService.salvar(reserva));

        assertEquals("Modal em manutenção não pode receber reservas.", exception.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a capacidade do modal for atingida")
    void deveLancarExcecaoCapacidadeAtingida() {
        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(cidadeService.buscarPorId(1L)).thenReturn(Optional.of(origem));
        when(cidadeService.buscarPorId(2L)).thenReturn(Optional.of(destino));
        when(modalService.buscarPorId(1L)).thenReturn(Optional.of(modal));
        when(reservaRepository.countByModal(modal)).thenReturn(50L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservaService.salvar(reserva));

        assertEquals("Capacidade do modal atingida.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro for criança sem acompanhante")
    void deveLancarExcecaoCriancaSemAcompanhante() {
        passageiro.setIdade(5);

        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(cidadeService.buscarPorId(1L)).thenReturn(Optional.of(origem));
        when(cidadeService.buscarPorId(2L)).thenReturn(Optional.of(destino));
        when(modalService.buscarPorId(1L)).thenReturn(Optional.of(modal));
        when(reservaRepository.countByModal(modal)).thenReturn(0L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservaService.salvar(reserva));

        assertEquals("Passageiros entre 2 e 10 anos devem possuir acompanhante.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o acompanhante for menor de idade")
    void deveLancarExcecaoAcompanhanteMenorDeIdade() {
        passageiro.setIdade(5);
        Passageiro acompanhante = new Passageiro();
        acompanhante.setId(2L);
        acompanhante.setIdade(20);
        reserva.setAcompanhante(acompanhante);

        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(passageiroService.buscarPorId(2L)).thenReturn(Optional.of(acompanhante));
        when(cidadeService.buscarPorId(1L)).thenReturn(Optional.of(origem));
        when(cidadeService.buscarPorId(2L)).thenReturn(Optional.of(destino));
        when(modalService.buscarPorId(1L)).thenReturn(Optional.of(modal));
        when(reservaRepository.countByModal(modal)).thenReturn(0L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservaService.salvar(reserva));

        assertEquals("O acompanhante deve possuir mais de 21 anos.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o acompanhante for o próprio passageiro")
    void deveLancarExcecaoAcompanhanteIgualPassageiro() {
        passageiro.setIdade(5);
        reserva.setAcompanhante(passageiro);

        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(passageiroService.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
        when(cidadeService.buscarPorId(1L)).thenReturn(Optional.of(origem));
        when(cidadeService.buscarPorId(2L)).thenReturn(Optional.of(destino));
        when(modalService.buscarPorId(1L)).thenReturn(Optional.of(modal));
        when(reservaRepository.countByModal(modal)).thenReturn(0L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservaService.salvar(reserva));

        assertEquals("O acompanhante não pode ser o próprio passageiro.", exception.getMessage());
    }
}