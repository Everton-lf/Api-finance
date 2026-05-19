package br.com.verso.caixa.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.math.BigDecimal;

@RegisterForReflection
public class SimulationRequestDTO {

    @NotNull(message = "valorInicial não pode ser nulo")
    @Positive(message = "valorInicial deve ser maior que zero")
    public BigDecimal valorInicial;

    @NotNull(message = "taxaJurosMensal não pode ser nula")
    @Positive(message = "taxaJurosMensal deve ser maior que zero")
    @DecimalMax(value = "100.00", message = "taxaJurosMensal não pode ser maior que 100")
    public BigDecimal taxaJurosMensal;

    @NotNull(message = "prazoMeses não pode ser nulo")
    @Positive(message = "prazoMeses deve ser maior que zero")
    public Integer prazoMeses;

    public SimulationRequestDTO() {
    }

    public SimulationRequestDTO(BigDecimal valorInicial, BigDecimal taxaJurosMensal, Integer prazoMeses) {
        this.valorInicial = valorInicial;
        this.taxaJurosMensal = taxaJurosMensal;
        this.prazoMeses = prazoMeses;
    }
}

