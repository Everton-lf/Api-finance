package br.com.verso.caixa.dto.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.math.BigDecimal;

@RegisterForReflection
public class CalculationMemoryDTO {

    public Integer mes;
    public BigDecimal saldoInicial;
    public BigDecimal jurosMes;
    public BigDecimal saldoFinal;

    public CalculationMemoryDTO() {
    }

    public CalculationMemoryDTO(Integer mes, BigDecimal saldoInicial, BigDecimal jurosMes, BigDecimal saldoFinal) {
        this.mes = mes;
        this.saldoInicial = saldoInicial;
        this.jurosMes = jurosMes;
        this.saldoFinal = saldoFinal;
    }
}

