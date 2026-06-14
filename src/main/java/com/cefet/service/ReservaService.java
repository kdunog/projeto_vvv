package com.cefet.service;

import com.cefet.entity.Funcionario;
import com.cefet.entity.Reserva;
import com.cefet.repository.ReservaRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;

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

        if (reserva.getCidadeOrigem() == null ||
            reserva.getCidadeDestino() == null) {

            throw new RuntimeException(
                    "Origem e destino são obrigatórios.");
        }

        if ("EM_MANUTENCAO".equals(
                reserva.getModal().getStatus())) {

            throw new RuntimeException(
                    "Modal em manutenção não pode receber reservas.");
        }

        long reservasNoModal =
                reservaRepository.countByModal(
                        reserva.getModal());

        if (reservasNoModal >=
            reserva.getModal().getCapacidade()) {

            throw new RuntimeException(
                    "Capacidade do modal atingida.");
        }

        Integer idade =
                reserva.getPassageiro().getIdade();

        if (idade != null &&
            idade > 2 &&
            idade < 10) {

            if (reserva.getAcompanhante() == null) {

                throw new RuntimeException(
                        "Passageiros entre 2 e 10 anos devem possuir acompanhante.");
            }

            Integer idadeAcompanhante =
                    reserva.getAcompanhante().getIdade();

            if (idadeAcompanhante == null ||
                idadeAcompanhante < 21) {

                throw new RuntimeException(
                        "O acompanhante deve possuir mais de 21 anos.");
            }

            if (reserva.getPassageiro().getId() != null &&
                reserva.getAcompanhante().getId() != null &&
                reserva.getPassageiro().getId()
                        .equals(reserva.getAcompanhante().getId())) {

                throw new RuntimeException(
                        "O acompanhante não pode ser o próprio passageiro.");
            }
        }

        reserva.setStatus("PENDENTE");

        return reservaRepository.save(reserva);
    }

    @Transactional
    public void deletar(Long id) {
        reservaRepository.deleteById(id);
    }

    @Transactional
    public Reserva confirmarVendaFisica(
            Long reservaId,
            Funcionario funcionario) {

        Reserva reserva =
                buscarPorId(reservaId);

        if (reserva.isVendaOnline()) {

            throw new RuntimeException(
                    "Apenas vendas físicas podem ser confirmadas por funcionário.");
        }

        reserva.setFuncionarioConfirmacao(
                funcionario);

        reserva.setDataConfirmacao(
                LocalDateTime.now());

        reserva.setConfirmada(true);

        reserva.setStatus("CONFIRMADA");

        return reservaRepository.save(
                reserva);
    }

    @Transactional
    public Reserva aprovarVendaOnline(
            Reserva reserva,
            Funcionario funcionario) {

        if (!reserva.isVendaOnline()) {

            throw new RuntimeException(
                    "A reserva não é online.");
        }

        reserva.setConfirmada(true);

        reserva.setFuncionarioConfirmacao(
                funcionario);

        reserva.setDataConfirmacao(
                LocalDateTime.now());

        reserva.setStatus("CONFIRMADA");

        return reservaRepository.save(
                reserva);
    }
}