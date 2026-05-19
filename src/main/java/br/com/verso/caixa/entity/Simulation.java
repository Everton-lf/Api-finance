package br.com.verso.caixa.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulations")
public class Simulation extends PanacheEntity {

    @Column(name = "valor_inicial", nullable = false)
    public BigDecimal valorInicial;

    @Column(name = "taxa_juros_mensal", nullable = false)
    public BigDecimal taxaJurosMensal;

    @Column(name = "prazo_meses", nullable = false)
    public Integer prazoMeses;

    @Column(name = "valor_total_final", nullable = false)
    public BigDecimal valorTotalFinal;

    @Column(name = "valor_total_juros", nullable = false)
    public BigDecimal valorTotalJuros;

    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public List<CalculationMemory> memories = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
}

