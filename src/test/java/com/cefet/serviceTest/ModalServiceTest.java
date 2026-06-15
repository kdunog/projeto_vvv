package com.cefet.serviceTest;

import com.cefet.entity.Modal;
import com.cefet.repository.ModalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.cefet.service.ModalService;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)

class ModalServiceTest {

    @InjectMocks
    private ModalService modalService;

    @Mock
    private ModalRepository modalRepository;

    @Test
    @DisplayName("Deve definir status como EM_MANUTENCAO se a data da última manutenção for futura (RN006)")
    void deveMudarStatusParaEmManutencao() {
        Modal modal = new Modal();
        modal.setTipo("Ônibus");
        modal.setUltimaManutencao(LocalDate.now().plusDays(1));

        when(modalRepository.save(any(Modal.class))).thenAnswer(inv -> inv.getArgument(0));

        Modal resultado = modalService.salvar(modal);

        assertEquals("EM_MANUTENCAO", resultado.getStatus());
        verify(modalRepository, times(1)).save(modal);
    }

    @Test
    @DisplayName("Deve manter status como DISPONIVEL se a data da manutenção for hoje ou passada")
    void deveManterStatusDisponivel() {
        Modal modal = new Modal();
        modal.setTipo("Avião");
        modal.setUltimaManutencao(LocalDate.now());

        when(modalRepository.save(any(Modal.class))).thenAnswer(inv -> inv.getArgument(0));

        Modal resultado = modalService.salvar(modal);

        assertEquals("DISPONIVEL", resultado.getStatus());
        verify(modalRepository, times(1)).save(modal);
    }
}