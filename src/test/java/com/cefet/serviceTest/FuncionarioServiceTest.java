package com.cefet.serviceTest;

import com.cefet.entity.Funcionario;
import com.cefet.entity.PontoVenda;
import com.cefet.repository.FuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.cefet.service.FuncionarioService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuncionarioServiceTest {

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve lançar exceção se funcionário não tiver nenhum ponto de venda")
    void deveLancarExcecaoSemPontoVenda() {
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(new ArrayList<>()); // Vazio

        RuntimeException exception = assertThrows(RuntimeException.class, () -> funcionarioService.salvar(funcionario));
        assertEquals("Funcionário deve possuir pelo menos um ponto de venda.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se funcionário for associado a mais de dois pontos de venda")
    void deveLancarExcecaoMaisDeDoisPontos() {
        Funcionario funcionario = new Funcionario();
        List<PontoVenda> pontos = List.of(new PontoVenda(), new PontoVenda(), new PontoVenda());
        funcionario.setPontosVenda(pontos);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> funcionarioService.salvar(funcionario));
        assertEquals("Funcionário não pode estar vinculado a mais de dois pontos de venda.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar associar a dois pontos sem autorização do gerente")
    void deveLancarExcecaoDoisPontosSemAutorizacao() {
        Funcionario funcionario = new Funcionario();
        List<PontoVenda> pontos = List.of(new PontoVenda(), new PontoVenda());
        funcionario.setPontosVenda(pontos);
        funcionario.setAutorizadoMultiplosPontos(false); // Sem autorização

        RuntimeException exception = assertThrows(RuntimeException.class, () -> funcionarioService.salvar(funcionario));
        assertEquals("Funcionário precisa de autorização do gerente para trabalhar em múltiplos pontos.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve salvar funcionário com sucesso em dois pontos se possuir autorização do gerente")
    void deveSalvarComDoisPontosEAutorizacao() {
        Funcionario funcionario = new Funcionario();
        List<PontoVenda> pontos = List.of(new PontoVenda(), new PontoVenda());
        funcionario.setPontosVenda(pontos);
        funcionario.setAutorizadoMultiplosPontos(true); // Autorizado

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        Funcionario salvo = funcionarioService.salvar(funcionario);

        assertNotNull(salvo);
        verify(funcionarioRepository, times(1)).save(funcionario);
    }
}