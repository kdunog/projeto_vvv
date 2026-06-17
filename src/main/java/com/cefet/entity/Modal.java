package com.cefet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Modal")
@Getter
@Setter
public class Modal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_da_transportadora", nullable = false)
    private Transportadora transportadora;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(name = "ultima_manutencao")
    private LocalDate ultimaManutencao;

    @Column(name = "em_manutencao", nullable = false)
    private boolean emManutencao;

    @Column(length = 30)
    private String status;
}