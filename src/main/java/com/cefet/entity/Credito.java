package com.cefet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Credito")
@Getter
@Setter
public class Credito {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Cartao cartao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_do_cartao", nullable = false)
    private Cartao cartaoAssociado;

    @Column(nullable = false)
    private Integer numeroDeParcelas;
}
