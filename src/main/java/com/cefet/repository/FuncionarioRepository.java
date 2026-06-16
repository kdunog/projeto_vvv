package com.cefet.repository;

import com.cefet.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByCpf(String cpf);
    @Query("""
    SELECT COUNT(f)
    FROM Funcionario f
    JOIN f.pontosVenda p
    WHERE p.id = :pontoVendaId
    """)
    long contarFuncionariosDoPonto(Long pontoVendaId);

}