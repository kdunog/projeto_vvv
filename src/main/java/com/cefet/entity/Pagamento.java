package com.cefet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pagamento")
@Getter
@Setter
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_da_reserva", nullable = false)
    private Reserva reserva;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credito")
    private Credito credito; // relação adicionada

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(precision = 10, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal juros = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Column(length = 50)
    private String status;

    @Column(nullable = false)
    private Boolean confirmadoPelaOperadora = false;

    private LocalDateTime dataConfirmacaoOperadora;
}

