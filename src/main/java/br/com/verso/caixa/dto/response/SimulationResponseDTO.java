package br.com.verso.caixa.dto.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RegisterForReflection
public class SimulationResponseDTO {

    public Long id;
    public BigDecimal valorInicial;
    public BigDecimal taxaJurosMensal;
    public Integer prazoMeses;
    public BigDecimal valorTotalFinal;
    public BigDecimal valorTotalJuros;
    public LocalDateTime dataCriacao;
    public List<CalculationMemoryDTO> memoriaCalculo;

    public SimulationResponseDTO() {
    }

    public SimulationResponseDTO(Long id, BigDecimal valorInicial, BigDecimal taxaJurosMensal,
                                 Integer prazoMeses, BigDecimal valorTotalFinal,
                                 BigDecimal valorTotalJuros, LocalDateTime dataCriacao,
                                 List<CalculationMemoryDTO> memoriaCalculo) {
        this.id = id;
        this.valorInicial = valorInicial;
        this.taxaJurosMensal = taxaJurosMensal;
        this.prazoMeses = prazoMeses;
        this.valorTotalFinal = valorTotalFinal;
        this.valorTotalJuros = valorTotalJuros;
        this.dataCriacao = dataCriacao;
        this.memoriaCalculo = memoriaCalculo;
    }
}

