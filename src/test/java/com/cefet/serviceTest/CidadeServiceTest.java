package com.cefet.serviceTest;

import com.cefet.entity.Cidade;
import com.cefet.service.CidadeService;
import com.cefet.repository.CidadeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)

class CidadeServiceTest {

    @InjectMocks
    private CidadeService cidadeService;

  

    @Mock
    private CidadeRepository cidadeRepository;

    @Test
    @DisplayName("Deve salvar cidade com sucesso convertendo nome para maiúsculo quando identificador for válido")
    void deveSalvarCidadeComSucesso() {
        Cidade cidade = new Cidade();
        cidade.setIndentificador("RIO");
        cidade.setNome("rio de janeiro");

        when(cidadeRepository.save(any(Cidade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cidade cidadeSalva = cidadeService.salvar(cidade);

        assertNotNull(cidadeSalva);
        assertEquals("RIO DE JANEIRO", cidadeSalva.getNome());
        verify(cidadeRepository, times(1)).save(cidade);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o identificador da cidade for menor que 3 caracteres")
    void deveLancarExcecaoIdentificadorCurto() {
        Cidade cidade = new Cidade();
        cidade.setIndentificador("RJ");
        cidade.setNome("Rio de Janeiro");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cidadeService.salvar(cidade));
        assertEquals("Identificador da cidade deve ter 3 letras maiúsculas.", exception.getMessage());
        verify(cidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o identificador da cidade contiver caracteres minúsculos")
    void deveLancarExcecaoIdentificadorMinusculo() {
        Cidade cidade = new Cidade();
        cidade.setIndentificador("Rio");
        cidade.setNome("Rio de Janeiro");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cidadeService.salvar(cidade));
        assertEquals("Identificador da cidade deve ter 3 letras maiúsculas.", exception.getMessage());
    }
}