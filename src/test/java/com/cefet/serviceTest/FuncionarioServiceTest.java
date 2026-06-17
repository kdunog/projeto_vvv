package com.cefet.serviceTest;
import com.cefet.entity.Funcionario;
import com.cefet.entity.PontoVenda;
import com.cefet.repository.FuncionarioRepository;
import com.cefet.service.FuncionarioService;
import com.cefet.service.PontoVendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
class FuncionarioServiceTest {
    @InjectMocks
    private FuncionarioService funcionarioService;
    @Mock
    private FuncionarioRepository funcionarioRepository;
    @Mock
    private PontoVendaService pontoVendaService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("Deve lançar exceção se funcionário não tiver nenhum ponto de venda")
    void deveLancarExcecaoSemPontoVenda() {
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(new ArrayList<>());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
funcionarioService.salvar(funcionario));
        assertEquals("Funcionário deve possuir pelo menos um ponto de venda.", 
exception.getMessage());
    }
    @Test
    @DisplayName("Deve lançar exceção se um ponto de venda não tiver ID")
    void deveLancarExcecaoPontoSemId() {
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(List.of(new PontoVenda()));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
funcionarioService.salvar(funcionario));
        assertEquals("Cada ponto de venda deve possuir um ID.", 
exception.getMessage());
    }
    @Test
    @DisplayName("Deve lançar exceção se um ponto de venda não existir")
    void deveLancarExcecaoPontoInexistente() {
        PontoVenda pontoVenda = new PontoVenda();
        pontoVenda.setId(1L);
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(List.of(pontoVenda));
        when(pontoVendaService.buscarPorId(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
funcionarioService.salvar(funcionario));
        assertEquals("Ponto de venda não encontrado: 1", 
exception.getMessage());
    }
    @Test
    @DisplayName("Deve lançar exceção se funcionário estiver vinculado a mais de dois pontos")
    void deveLancarExcecaoMaisDeDoisPontos() {
        PontoVenda ponto1 = new PontoVenda();
        ponto1.setId(1L);
        PontoVenda ponto2 = new PontoVenda();
        ponto2.setId(2L);
        PontoVenda ponto3 = new PontoVenda();
        ponto3.setId(3L);
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(List.of(ponto1, ponto2, ponto3));
        when(pontoVendaService.buscarPorId(1L)).thenReturn(Optional.of(ponto1));
        when(pontoVendaService.buscarPorId(2L)).thenReturn(Optional.of(ponto2));
        when(pontoVendaService.buscarPorId(3L)).thenReturn(Optional.of(ponto3));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
funcionarioService.salvar(funcionario));
        assertEquals("Funcionário não pode estar vinculado a mais de dois pontos de venda.", exception.getMessage());
    }
    @Test
    @DisplayName("Deve salvar funcionário com cargo GERENTE quando não houver funcionários no ponto")
    void deveSalvarComCargoGerente() {
        PontoVenda ponto = new PontoVenda();
        ponto.setId(1L);
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(List.of(ponto));
        when(pontoVendaService.buscarPorId(1L)).thenReturn(Optional.of(ponto));
        
when(funcionarioRepository.contarFuncionariosDoPonto(1L)).thenReturn(0L);
        
when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Funcionario salvo = funcionarioService.salvar(funcionario);
        assertNotNull(salvo);
        assertEquals("GERENTE", salvo.getCargo());
        verify(funcionarioRepository, times(1)).save(funcionario);
    }
    @Test
    @DisplayName("Deve salvar funcionário com cargo VENDEDOR quando já houver funcionário no ponto")
    void deveSalvarComCargoVendedor() {
        PontoVenda ponto = new PontoVenda();
        ponto.setId(1L);
        Funcionario funcionario = new Funcionario();
        funcionario.setPontosVenda(List.of(ponto));
        when(pontoVendaService.buscarPorId(1L)).thenReturn(Optional.of(ponto));
        
when(funcionarioRepository.contarFuncionariosDoPonto(1L)).thenReturn(1L);
        
when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Funcionario salvo = funcionarioService.salvar(funcionario);
        assertEquals("VENDEDOR", salvo.getCargo());
    }
}