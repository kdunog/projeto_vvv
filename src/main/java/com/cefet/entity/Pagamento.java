package com.cefet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_da_reserva", nullable = false)
    @JsonIgnoreProperties({"pagamento", "ticket"})
    private Reserva reserva;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_credito")
    private Credito credito; // relação adicionada

    @Column(length = 50)
    private String metodoPagamento;

    @Column(name = "numero_de_parcelas")
    private Integer numeroDeParcelas;

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

