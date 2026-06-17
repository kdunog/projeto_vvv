package com.cefet.serviceTest;

import com.cefet.entity.Cidade;
import com.cefet.repository.CidadeRepository;
import com.cefet.service.CidadeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CidadeServiceTest {

    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private CidadeService cidadeService;

    @Test
    @DisplayName("Deve salvar cidade com sucesso convertendo o nome para maiúsculo")
    void deveSalvarCidadeComSucesso() {
        Cidade cidade = new Cidade();
        cidade.setIndentificador("SPO");
        cidade.setNome("sao paulo");

        when(cidadeRepository.save(any(Cidade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cidade cidadeSalva = cidadeService.salvar(cidade);

        assertNotNull(cidadeSalva);
        assertEquals("SPO", cidadeSalva.getIndentificador());
        assertEquals("SAO PAULO", cidadeSalva.getNome());
        verify(cidadeRepository, times(1)).save(cidade);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o identificador da cidade tiver menos de 3 caracteres")
    void deveLancarExcecaoIdentificadorCurto() {
        Cidade cidade = new Cidade();
        cidade.setIndentificador("SP");
        cidade.setNome("Sao Paulo");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cidadeService.salvar(cidade));

        assertEquals("Identificador da cidade deve ter 3 letras maiúsculas.", exception.getMessage());
        verify(cidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar cidade por nome")
    void deveBuscarCidadePorNome() {
        Cidade cidade = new Cidade();
        cidade.setNome("CURITIBA");

        when(cidadeRepository.findByNome("CURITIBA")).thenReturn(cidade);

        Cidade resultado = cidadeService.buscarPorNome("CURITIBA");

        assertNotNull(resultado);
        assertEquals("CURITIBA", resultado.getNome());
    }
}