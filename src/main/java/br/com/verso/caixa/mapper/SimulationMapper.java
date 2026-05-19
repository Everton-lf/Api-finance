package br.com.verso.caixa.mapper;

import br.com.verso.caixa.dto.response.CalculationMemoryDTO;
import br.com.verso.caixa.dto.response.SimulationResponseDTO;
import br.com.verso.caixa.entity.CalculationMemory;
import br.com.verso.caixa.entity.Simulation;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimulationMapper {

    public SimulationResponseDTO toResponseDTO(Simulation simulation) {
        if (simulation == null) {
            return null;
        }

        var memoriaCalculo = simulation.memories
                .stream()
                .map(this::toMemoryDTO)
                .toList();

        return new SimulationResponseDTO(
                simulation.id,
                simulation.valorInicial,
                simulation.taxaJurosMensal,
                simulation.prazoMeses,
                simulation.valorTotalFinal,
                simulation.valorTotalJuros,
                simulation.dataCriacao,
                memoriaCalculo
        );
    }

    public CalculationMemoryDTO toMemoryDTO(CalculationMemory memory) {
        if (memory == null) {
            return null;
        }

        return new CalculationMemoryDTO(
                memory.mes,
                memory.saldoInicial,
                memory.jurosMes,
                memory.saldoFinal
        );
    }
}

