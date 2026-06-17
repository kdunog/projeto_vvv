package com.cefet.controllerTest;

import com.cefet.controller.CidadeController;
import com.cefet.controller.GlobalExceptionHandler;
import com.cefet.entity.Cidade;
import com.cefet.service.CidadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CidadeControllerTest {

    @Mock
    private CidadeService iCidadeService;

    @InjectMocks
    private CidadeController cidadeController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(cidadeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Deve retornar 200 OK quando o payload da cidade for válido")
    void deveRetornarSucessoNoCadastro() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setIndentificador("RIO");
        cidade.setNome("RIO DE JANEIRO");

        when(iCidadeService.salvar(any(Cidade.class))).thenReturn(cidade);

        mockMvc.perform(post("/cidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cidade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.indentificador").value("RIO"))
                .andExpect(jsonPath("$.nome").value("RIO DE JANEIRO"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se a Service lançar exceção de validação")
    void deveRetornarBadRequestQuandoHouverErroDeNegocio() throws Exception {
        Cidade cidadeInvalida = new Cidade();
        cidadeInvalida.setIndentificador("RJ");
        cidadeInvalida.setNome("Rio");

        when(iCidadeService.salvar(any(Cidade.class)))
                .thenThrow(new RuntimeException("Identificador da cidade deve ter 3 letras maiúsculas."));

        mockMvc.perform(post("/cidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cidadeInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Identificador da cidade deve ter 3 letras maiúsculas."));
    }
}
