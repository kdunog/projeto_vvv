package com.cefet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Pagamento pagamento;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    private Ticket ticket;

    @Column(nullable = false)
    private boolean vendaOnline;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_confirmacao")
    private Funcionario funcionarioConfirmacao;

    private LocalDateTime dataConfirmacao;

    private Boolean confirmada = false;

    @Column(length = 20)
    private String tipoVenda;
}