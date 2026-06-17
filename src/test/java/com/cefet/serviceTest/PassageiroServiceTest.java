package com.cefet.serviceTest;

import com.cefet.entity.Passageiro;
import com.cefet.repository.PassageiroRepository;
import com.cefet.service.PassageiroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassageiroServiceTest {

    @Mock
    private PassageiroRepository passageiroRepository;

    @InjectMocks
    private PassageiroService passageiroService;

    @Test
    @DisplayName("Deve salvar passageiro")
    void deveSalvarPassageiro() {
        Passageiro passageiro = new Passageiro();
        passageiro.setNome("Maria Silva");
        passageiro.setCpf("123.456.789-00");

        when(passageiroRepository.save(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Passageiro resultado = passageiroService.salvar(passageiro);

        assertNotNull(resultado);
        assertEquals("Maria Silva", resultado.getNome());
        verify(passageiroRepository, times(1)).save(passageiro);
    }

    @Test
    @DisplayName("Deve listar todos os passageiros")
    void deveListarTodosPassageiros() {
        when(passageiroRepository.findAll()).thenReturn(List.of(new Passageiro(), new Passageiro()));

        assertEquals(2, passageiroService.listarTodos().size());
    }

    @Test
    @DisplayName("Deve buscar passageiro por CPF")
    void deveBuscarPassageiroPorCpf() {
        Passageiro passageiro = new Passageiro();
        passageiro.setCpf("111.222.333-44");

        when(passageiroRepository.findByCpf("111.222.333-44")).thenReturn(Optional.of(passageiro));

        Optional<Passageiro> resultado = passageiroService.buscarPorCpf("111.222.333-44");

        assertTrue(resultado.isPresent());
        assertEquals("111.222.333-44", resultado.get().getCpf());
    }

    @Test
    @DisplayName("Deve retornar vazio quando CPF não existir")
    void deveRetornarVazioParaCpfInexistente() {
        when(passageiroRepository.findByCpf("000.000.000-00")).thenReturn(Optional.empty());

        Optional<Passageiro> resultado = passageiroService.buscarPorCpf("000.000.000-00");

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve deletar passageiro")
    void deveDeletarPassageiro() {
        doNothing().when(passageiroRepository).deleteById(1L);

        passageiroService.deletar(1L);

        verify(passageiroRepository, times(1)).deleteById(1L);
    }
}