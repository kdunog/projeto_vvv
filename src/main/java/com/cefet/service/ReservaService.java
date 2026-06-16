package com.cefet.service;

import com.cefet.entity.Cidade;
import com.cefet.entity.Funcionario;
import com.cefet.entity.Modal;
import com.cefet.entity.Passageiro;
import com.cefet.entity.Reserva;
import com.cefet.repository.ReservaRepository;
import com.cefet.service.CidadeService;
import com.cefet.service.ModalService;
import com.cefet.service.PassageiroService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final PassageiroService passageiroService;
    private final CidadeService cidadeService;
    private final ModalService modalService;

    @Transactional(readOnly = true)
    public List<Reserva> listarTodos() {
        return reservaRepository.findAllCompleto();
    }

    @Transactional(readOnly = true)
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findByIdCompleto(id)
                .orElseThrow(() ->
                        new RuntimeException("Reserva não encontrada!"));
    }

    @Transactional
    public Reserva salvar(Reserva reserva) {

        if (reserva.getPassageiro() == null || reserva.getPassageiro().getId() == null) {
            throw new RuntimeException("Passageiro é obrigatório.");
        }

        if (reserva.getCidadeOrigem() == null || reserva.getCidadeOrigem().getId() == null ||
                reserva.getCidadeDestino() == null || reserva.getCidadeDestino().getId() == null) {
            throw new RuntimeException("Origem e destino são obrigatórios.");
        }

        if (reserva.getModal() == null || reserva.getModal().getId() == null) {
            throw new RuntimeException("Modal é obrigatório.");
        }

        Passageiro passageiro = passageiroService.buscarPorId(reserva.getPassageiro().getId())
                .orElseThrow(() -> new RuntimeException("Passageiro não encontrado."));

        Cidade cidadeOrigem = cidadeService.buscarPorId(reserva.getCidadeOrigem().getId())
                .orElseThrow(() -> new RuntimeException("Cidade de origem não encontrada."));

        Cidade cidadeDestino = cidadeService.buscarPorId(reserva.getCidadeDestino().getId())
                .orElseThrow(() -> new RuntimeException("Cidade de destino não encontrada."));

        Modal modal = modalService.buscarPorId(reserva.getModal().getId())
                .orElseThrow(() -> new RuntimeException("Modal não encontrado."));

        if ("EM_MANUTENCAO".equals(modal.getStatus())) {
            throw new RuntimeException("Modal em manutenção não pode receber reservas.");
        }

        long reservasNoModal = reservaRepository.countByModal(modal);

        if (reservasNoModal >= modal.getCapacidade()) {
            throw new RuntimeException("Capacidade do modal atingida.");
        }

        Integer idade = passageiro.getIdade();

        if (idade != null && idade > 2 && idade < 10) {
            if (reserva.getAcompanhante() == null || reserva.getAcompanhante().getId() == null) {
                throw new RuntimeException("Passageiros entre 2 e 10 anos devem possuir acompanhante.");
            }

            Passageiro acompanhante = passageiroService.buscarPorId(reserva.getAcompanhante().getId())
                    .orElseThrow(() -> new RuntimeException("Acompanhante não encontrado."));

            Integer idadeAcompanhante = acompanhante.getIdade();

            if (idadeAcompanhante == null || idadeAcompanhante < 21) {
                throw new RuntimeException("O acompanhante deve possuir mais de 21 anos.");
            }

            if (passageiro.getId() != null && acompanhante.getId() != null &&
                    passageiro.getId().equals(acompanhante.getId())) {
                throw new RuntimeException("O acompanhante não pode ser o próprio passageiro.");
            }

            reserva.setAcompanhante(acompanhante);
        }

        reserva.setPassageiro(passageiro);
        reserva.setCidadeOrigem(cidadeOrigem);
        reserva.setCidadeDestino(cidadeDestino);
        reserva.setModal(modal);

        reserva.setStatus("PENDENTE");
        reserva.setConfirmada(false);

        return reservaRepository.save(reserva);
    }

    @Transactional
    public void deletar(Long id) {
        reservaRepository.deleteById(id);
    }

    // RN016
    @Transactional
    public Reserva confirmarVendaFisica(
            Long reservaId,
            Funcionario funcionario) {

        Reserva reserva = buscarPorId(reservaId);

        if (reserva.isVendaOnline()) {

            throw new RuntimeException(
                    "Apenas vendas físicas podem ser confirmadas por funcionário.");
        }

        reserva.setFuncionarioConfirmacao(funcionario);

        reserva.setDataConfirmacao(
                LocalDateTime.now());

        reserva.setConfirmada(true);

        reserva.setStatus("CONFIRMADA");

        return reservaRepository.save(reserva);
    }
}