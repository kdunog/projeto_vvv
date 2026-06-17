package com.cefet.controlleTest;
import com.cefet.controller.PassageiroController;
import com.cefet.entity.Passageiro;
import com.cefet.service.PassageiroService;
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
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static 
org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static 
org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static 
org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static 
org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class PassageiroControllerTest {
@Mock
private PassageiroService service;
@InjectMocks
private PassageiroController controller;
private MockMvc mockMvc;
private final ObjectMapper objectMapper = new ObjectMapper();
@BeforeEach
void setUp() {
mockMvc = MockMvcBuilders
.standaloneSetup(controller).build();
}
@Test
@DisplayName("Deve listar passageiros")
void deveListarPassageiros() throws Exception {
Passageiro passageiro = new Passageiro();
passageiro.setId(1L);
passageiro.setNome("Ana");
passageiro.setCpf("123.456.789-00");
  when(service.listarTodos()).thenReturn(List.of(passageiro));
  mockMvc.perform(get("/passageiros"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].id").value(1))
    .andExpect(jsonPath("$[0].nome").value("Ana"));
 }
 @Test
 @DisplayName("Deve buscar passageiro por id")
 void deveBuscarPassageiroPorId() throws Exception {
  Passageiro passageiro = new Passageiro();
  passageiro.setId(1L);
  passageiro.setNome("Ana");
  
when(service.buscarPorId(1L)).thenReturn(Optional.of(passageiro));
  mockMvc.perform(get("/passageiros/1"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id").value(1))
    .andExpect(jsonPath("$.nome").value("Ana"));
 }
 @Test
 @DisplayName("Deve retornar 404 quando passageiro por id nao existir")
 void deveRetornar404QuandoPassageiroPorIdNaoExistir() throws Exception {
  when(service.buscarPorId(99L)).thenReturn(Optional.empty());
  mockMvc.perform(get("/passageiros/99"))
    .andExpect(status().isNotFound());
 }
 @Test
 @DisplayName("Deve salvar passageiro")
 void deveSalvarPassageiro() throws Exception {
  Passageiro passageiro = new Passageiro();
  passageiro.setNome("Ana");
  passageiro.setCpf("123.456.789-00");
  
when(service.salvar(any(Passageiro.class))).thenAnswer(invocation -> 
invocation.getArgument(0));
  mockMvc.perform(post("/passageiros")
      
.contentType(MediaType.APPLICATION_JSON)
      
.content(objectMapper.writeValueAsString(passageiro)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.nome").value("Ana"))
    
.andExpect(jsonPath("$.cpf").value("123.456.789-00"));
 }
 @Test
 @DisplayName("Deve atualizar passageiro quando existir")
 void deveAtualizarPassageiroQuandoExistir() throws Exception {
  Passageiro existente = new Passageiro();
  existente.setId(1L);
  existente.setNome("Ana");
  existente.setCpf("123.456.789-00");
  Passageiro atualizado = new Passageiro();
  atualizado.setNome("Ana Silva");
  atualizado.setCpf("123.456.789-00");
  
when(service.buscarPorId(1L)).thenReturn(Optional.of(existente));
  
when(service.salvar(any(Passageiro.class))).thenAnswer(invocation -> 
invocation.getArgument(0));
  mockMvc.perform(put("/passageiros/1")
      
.contentType(MediaType.APPLICATION_JSON)
      
.content(objectMapper.writeValueAsString(atualizado)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id").value(1))
    .andExpect(jsonPath("$.nome").value("Ana Silva"));
 }
 @Test
 @DisplayName("Deve retornar 404 ao atualizar passageiro inexistente")
 void deveRetornar404AoAtualizarPassageiroInexistente() throws Exception 
{
  Passageiro atualizado = new Passageiro();
  atualizado.setNome("Ana Silva");
  when(service.buscarPorId(1L)).thenReturn(Optional.empty());
  mockMvc.perform(put("/passageiros/1")
      
.contentType(MediaType.APPLICATION_JSON)
      
.content(objectMapper.writeValueAsString(atualizado)))
    .andExpect(status().isNotFound());
 }
 @Test
 @DisplayName("Deve atualizar parcialmente passageiro quando existir")
 void deveAtualizarParcialmentePassageiroQuandoExistir() throws Exception
{
  Passageiro existente = new Passageiro();
  existente.setId(1L);
  existente.setNome("Ana");
  existente.setCpf("123.456.789-00");
  existente.setTelefone("1111-1111");
  Passageiro dadosAtualizados = new Passageiro();
  dadosAtualizados.setNome("Ana Silva");
  
when(service.buscarPorId(1L)).thenReturn(Optional.of(existente));
  
when(service.salvar(any(Passageiro.class))).thenAnswer(invocation -> 
invocation.getArgument(0));
  mockMvc.perform(patch("/passageiros/1")
      
.contentType(MediaType.APPLICATION_JSON)
      
.content(objectMapper.writeValueAsString(dadosAtualizados)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id").value(1))
    .andExpect(jsonPath("$.nome").value("Ana Silva"))
    
.andExpect(jsonPath("$.cpf").value("123.456.789-00"))
    
.andExpect(jsonPath("$.telefone").value("1111-1111"));
 }
 @Test
 @DisplayName("Deve retornar 404 ao atualizar parcialmente passageiro inexistente")
 void deveRetornar404AoAtualizarParcialmentePassageiroInexistente() 
throws Exception {
  Passageiro dadosAtualizados = new Passageiro();
  dadosAtualizados.setNome("Ana Silva");
  when(service.buscarPorId(1L)).thenReturn(Optional.empty());
  mockMvc.perform(patch("/passageiros/1")
      
.contentType(MediaType.APPLICATION_JSON)
      
.content(objectMapper.writeValueAsString(dadosAtualizados)))
    .andExpect(status().isNotFound());
 }
 @Test
 @DisplayName("Deve deletar passageiro")
 void deveDeletarPassageiro() throws Exception {
  doNothing().when(service).deletar(1L);
  mockMvc.perform(delete("/passageiros/1"))
    .andExpect(status().isNoContent())
    .andExpect(content().string(""));
}
@Test
@DisplayName("Deve buscar passageiro por CPF")
void deveBuscarPassageiroPorCpf() throws Exception {
Passageiro passageiro = new Passageiro();
passageiro.setId(1L);
passageiro.setNome("Ana");
passageiro.setCpf("123.456.789-00");
when(service.buscarPorCpf("123.456.789-00")).thenReturn(Optional.of(passageiro))
;
mockMvc.perform(get("/passageiros/cpf/123.456.789-00"))
.andExpect(status().isOk())
.andExpect(jsonPath("$.id").value(1))
.andExpect(jsonPath("$.cpf").value("123.456.789-00"));
}
@Test
@DisplayName("Deve retornar 404 quando passageiro por CPF nao existir")
void deveRetornar404QuandoPassageiroPorCpfNaoExistir() throws Exception 
{
when(service.buscarPorCpf("123.456.789-00")).thenReturn(Optional.empty());
mockMvc.perform(get("/passageiros/cpf/123.456.789-00"))
.andExpect(status().isNotFound());
}
}