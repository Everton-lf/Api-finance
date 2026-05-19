package br.com.verso.caixa.service;

import br.com.verso.caixa.dto.request.SimulationRequestDTO;
import br.com.verso.caixa.dto.response.SimulationResponseDTO;
import br.com.verso.caixa.entity.CalculationMemory;
import br.com.verso.caixa.entity.Simulation;
import br.com.verso.caixa.exception.BusinessException;
import br.com.verso.caixa.exception.NotFoundException;
import br.com.verso.caixa.mapper.SimulationMapper;
import br.com.verso.caixa.repository.SimulationRepository;
import br.com.verso.caixa.util.FinancialCalculator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulationService {

    private static final Logger LOG = Logger.getLogger(SimulationService.class);

    private final SimulationRepository repository;
    private final SimulationMapper mapper;


    public List<SimulationResponseDTO> getAllSimulations() {
        List<Simulation> simulations = repository.findAllWithMemories();
        return simulations.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public SimulationService(SimulationRepository repository, SimulationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public SimulationResponseDTO createSimulation(SimulationRequestDTO request) {
        LOG.infof("Creating simulation with initial value: %s", request.valorInicial);

        validateRequest(request);

        Simulation simulation = new Simulation();
        simulation.valorInicial = FinancialCalculator.setScale(request.valorInicial);
        simulation.taxaJurosMensal = FinancialCalculator.setScale(request.taxaJurosMensal);
        simulation.prazoMeses = request.prazoMeses;

        // Calculate monthly memory and final values
        List<CalculationMemory> memories = new ArrayList<>();
        BigDecimal saldoAtual = simulation.valorInicial;
        BigDecimal totalJuros = BigDecimal.ZERO;

        for (int mes = 1; mes <= simulation.prazoMeses; mes++) {
            BigDecimal jurosMes = FinancialCalculator.calculateMonthlyInterest(
                    saldoAtual,
                    simulation.taxaJurosMensal
            );

            BigDecimal saldoFinal = FinancialCalculator.addValues(saldoAtual, jurosMes);
            totalJuros = FinancialCalculator.addValues(totalJuros, jurosMes);

            CalculationMemory memory = new CalculationMemory();
            memory.mes = mes;
            memory.saldoInicial = saldoAtual;
            memory.jurosMes = jurosMes;
            memory.saldoFinal = saldoFinal;
            memory.simulation = simulation;

            memories.add(memory);
            saldoAtual = saldoFinal;
        }

        simulation.memories = memories;
        simulation.valorTotalFinal = saldoAtual;
        simulation.valorTotalJuros = totalJuros;

        repository.persist(simulation);
        LOG.infof("Simulation created successfully with id: %d", simulation.id);

        return mapper.toResponseDTO(simulation);
    }

    public SimulationResponseDTO getSimulation(Long id) {
        LOG.infof("Retrieving simulation with id: %d", id);

        Simulation simulation = repository.findById(id);
        if (simulation == null) {
            LOG.warnf("Simulation with id %d not found", id);
            throw new NotFoundException("Simulação não encontrada com id: " + id);
        }

        return mapper.toResponseDTO(simulation);
    }

    private void validateRequest(SimulationRequestDTO request) {
        if (request.valorInicial.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("valorInicial deve ser maior que zero");
        }

        if (request.taxaJurosMensal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("taxaJurosMensal deve ser maior que zero");
        }

        if (request.taxaJurosMensal.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("taxaJurosMensal não pode ser maior que 100");
        }

        if (request.prazoMeses <= 0) {
            throw new BusinessException("prazoMeses deve ser maior que zero");
        }

        if (request.prazoMeses > 600) {
            throw new BusinessException("prazoMeses não pode ser maior que 600");
        }
    }
}

