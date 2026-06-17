package com.cefet.serviceTest;

import com.cefet.entity.Endereco;
import com.cefet.entity.PontoVenda;
import com.cefet.repository.PontoVendaRepository;
import com.cefet.service.EnderecoService;
import com.cefet.service.PontoVendaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PontoVendaServiceTest {

    @Mock
    private PontoVendaRepository pontoVendaRepository;

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private PontoVendaService pontoVendaService;

    @Test
    @DisplayName("Deve lançar exceção quando o endereço estiver nulo")
    void deveLancarExcecaoEnderecoNulo() {
        PontoVenda pontoVenda = new PontoVenda();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pontoVendaService.salvar(pontoVenda));

        assertEquals("Ponto de venda deve possuir endereço válido.", exception.getMessage());
        verify(pontoVendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o endereço não tiver ID")
    void deveLancarExcecaoEnderecoSemId() {
        PontoVenda pontoVenda = new PontoVenda();
        pontoVenda.setEndereco(new Endereco());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pontoVendaService.salvar(pontoVenda));

        assertEquals("Ponto de venda deve possuir endereço válido.", exception.getMessage());
        verify(pontoVendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o endereço não existir no banco")
    void deveLancarExcecaoEnderecoNaoEncontrado() {
        Endereco endereco = new Endereco();
        endereco.setId(99L);

        PontoVenda pontoVenda = new PontoVenda();
        pontoVenda.setEndereco(endereco);

        when(enderecoService.buscarPorId(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pontoVendaService.salvar(pontoVenda));

        assertEquals("Endereço não encontrado.", exception.getMessage());
        verify(pontoVendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve salvar ponto de venda com endereço válido")
    void deveSalvarComEnderecoValido() {
        Endereco endereco = new Endereco();
        endereco.setId(1L);

        PontoVenda pontoVenda = new PontoVenda();
        pontoVenda.setEndereco(endereco);

        when(enderecoService.buscarPorId(1L)).thenReturn(Optional.of(endereco));
        when(pontoVendaRepository.save(any(PontoVenda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PontoVenda resultado = pontoVendaService.salvar(pontoVenda);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getEndereco().getId());
        verify(pontoVendaRepository, times(1)).save(pontoVenda);
    }
}