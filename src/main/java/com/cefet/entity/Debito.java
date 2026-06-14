package com.cefet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Debito")
@Getter
@Setter
public class Debito extends Cartao {

    @Id
    private Long id;

//@OneToOne(fetch = FetchType.LAZY)
// @MapsId
// @JoinColumn(name = "id")
// private Cartao cartaoPrincipal;

//@ManyToOne(fetch = FetchType.LAZY, optional = false)
// @JoinColumn(name = "id_do_cartao", nullable = false)
// private Cartao cartaoAssociado;
}
