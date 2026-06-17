package com.cefet.serviceTest;

import com.cefet.entity.Modal;
import com.cefet.repository.ModalRepository;
import com.cefet.service.ModalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModalServiceTest {

    @Mock
    private ModalRepository modalRepository;

    @InjectMocks
    private ModalService modalService;

    @Test
    @DisplayName("Deve definir status como EM_MANUTENCAO quando o modal estiver em manutenção")
    void deveMudarStatusParaEmManutencao() {
        Modal modal = new Modal();
        modal.setTipo("Ônibus");
        modal.setCapacidade(50);
        modal.setEmManutencao(true);

        when(modalRepository.save(any(Modal.class))).thenAnswer(inv -> inv.getArgument(0));

        Modal resultado = modalService.salvar(modal);

        assertEquals("EM_MANUTENCAO", resultado.getStatus());
        verify(modalRepository, times(1)).save(modal);
    }

    @Test
    @DisplayName("Deve definir status como DISPONIVEL quando o modal não estiver em manutenção")
    void deveManterStatusDisponivel() {
        Modal modal = new Modal();
        modal.setTipo("Avião");
        modal.setCapacidade(120);
        modal.setEmManutencao(false);

        when(modalRepository.save(any(Modal.class))).thenAnswer(inv -> inv.getArgument(0));

        Modal resultado = modalService.salvar(modal);

        assertEquals("DISPONIVEL", resultado.getStatus());
        verify(modalRepository, times(1)).save(modal);
    }

    @Test
    @DisplayName("Deve retornar vazio quando o modal não existir")
    void deveRetornarVazioParaIdInexistente() {
        when(modalRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertTrue(modalService.buscarPorId(99L).isEmpty());
    }
}