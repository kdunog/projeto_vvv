package com.cefet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Reserva")
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_do_passageiro", nullable = false)
    private Passageiro passageiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_acompanhante")
    private Passageiro acompanhante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_da_cidade_origem", nullable = false)
    private Cidade cidadeOrigem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_da_cidade_destino", nullable = false)
    private Cidade cidadeDestino;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_do_modal", nullable = false)
    private Modal modal;

    @Column(name = "data_reserva", nullable = false)
    private LocalDate dataReserva;

    @Column(length = 50)
    private String status;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    @JsonIgnore
    private Pagamento pagamento;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    @JsonIgnore
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_confirmacao")
    private Funcionario funcionarioConfirmacao;

    private LocalDateTime dataConfirmacao;

    private Boolean confirmada = false;

    @ManyToOne
    @JoinColumn(name = "id_gerente_virtual")
    private Funcionario gerenteVirtual;

    private Boolean aprovadaPeloGerente = false;
}