package com.cefet.controlleTest;
import com.cefet.controller.ReservaController;
import com.cefet.entity.Reserva;
import com.cefet.service.ReservaService;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static 
org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static 
org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static 
org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {
    @Mock
    private ReservaService reservaService;
    @InjectMocks
    private ReservaController reservaController;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(reservaController)
                .build();
    }
    @Test
    @DisplayName("Deve retornar lista de reservas")
    void deveListarReservas() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setStatus("PENDENTE");
        when(reservaService.listarTodos()).thenReturn(List.of(reserva));
        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
    @Test
    @DisplayName("Deve retornar reserva por id")
    void deveRetornarReservaPorId() throws Exception {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        when(reservaService.buscarPorId(1L)).thenReturn(reserva);
        mockMvc.perform(get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    @DisplayName("Deve retornar 404 para reserva inexistente")
    void deveRetornar404ParaReservaInexistente() throws Exception {
        when(reservaService.buscarPorId(99L)).thenThrow(new 
RuntimeException("Reserva não encontrada!"));
        mockMvc.perform(get("/reservas/99"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Deve criar reserva")
    void deveCriarReserva() throws Exception {
        Reserva reserva = new Reserva();
        when(reservaService.salvar(any(Reserva.class))).thenAnswer(invocation ->
invocation.getArgument(0));
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Deve retornar 400 quando a regra de negócio falhar")
    void deveRetornar400QuandoRegraFalhar() throws Exception {
        Reserva reserva = new Reserva();
        when(reservaService.salvar(any(Reserva.class))).thenThrow(new 
RuntimeException("Modal em manutenção não pode receber reservas."));
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Modal em manutenção não pode receber reservas."));
    }
    @Test
    @DisplayName("Deve deletar reserva")
    void deveDeletarReserva() throws Exception {
        doNothing().when(reservaService).deletar(1L);
mockMvc.perform(delete("/reservas/1"))
.andExpect(status().isNoContent());
}
}