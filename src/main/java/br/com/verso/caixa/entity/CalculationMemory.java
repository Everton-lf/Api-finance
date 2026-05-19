package br.com.verso.caixa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "calculation_memories")
public class CalculationMemory extends PanacheEntity {

    @Column(name = "mes", nullable = false)
    public Integer mes;

    @Column(name = "saldo_inicial", nullable = false)
    public BigDecimal saldoInicial;

    @Column(name = "juros_mes", nullable = false)
    public BigDecimal jurosMes;

    @Column(name = "saldo_final", nullable = false)
    public BigDecimal saldoFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulation_id", nullable = false)
    @JsonBackReference
    public Simulation simulation;
}

