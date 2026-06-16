package com.cefet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "PontoVenda")
@Getter
@Setter
public class PontoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_endereco", nullable = false)
    private Endereco endereco;

    @Column(length = 20)
    private String telefone;

    @Column(length = 18)
    private String cnpj;
}