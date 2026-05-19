package br.com.verso.caixa.service;

import br.com.verso.caixa.dto.request.SimulationRequestDTO;
import br.com.verso.caixa.dto.response.SimulationResponseDTO;
import br.com.verso.caixa.entity.Simulation;
import br.com.verso.caixa.exception.BusinessException;
import br.com.verso.caixa.exception.NotFoundException;
import br.com.verso.caixa.mapper.SimulationMapper;
import br.com.verso.caixa.repository.SimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SimulationService Tests")
class SimulationServiceTest {

    @Mock
    private SimulationRepository repository;

    @Mock
    private SimulationMapper mapper;

    @InjectMocks
    private SimulationService service;

    private SimulationRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new SimulationRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("1.5"),
                12
        );
    }

    @Test
    @DisplayName("Should create simulation successfully with valid data")
    void shouldCreateSimulationSuccessfully() {
        when(mapper.toResponseDTO(any(Simulation.class))).thenReturn(new SimulationResponseDTO());

        SimulationResponseDTO response = service.createSimulation(validRequest);

        assertNotNull(response);
        verify(repository).persist(any(Simulation.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when valorInicial is zero")
    void shouldThrowBusinessExceptionWhenValorInicialIsZero() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                BigDecimal.ZERO,
                new BigDecimal("1.5"),
                12
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw BusinessException when valorInicial is negative")
    void shouldThrowBusinessExceptionWhenValorInicialIsNegative() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                new BigDecimal("-1000.00"),
                new BigDecimal("1.5"),
                12
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw BusinessException when taxaJurosMensal is negative")
    void shouldThrowBusinessExceptionWhenTaxaIsNegative() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("-1.5"),
                12
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw BusinessException when taxaJurosMensal exceeds 100")
    void shouldThrowBusinessExceptionWhenTaxaExceedsHundred() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("150.00"),
                12
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw BusinessException when prazoMeses is zero")
    void shouldThrowBusinessExceptionWhenPrazoIsZero() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("1.5"),
                0
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw BusinessException when prazoMeses is negative")
    void shouldThrowBusinessExceptionWhenPrazoIsNegative() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("1.5"),
                -12
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw BusinessException when prazoMeses exceeds 600")
    void shouldThrowBusinessExceptionWhenPrazoExceedsSixHundred() {
        SimulationRequestDTO invalidRequest = new SimulationRequestDTO(
                new BigDecimal("1000.00"),
                new BigDecimal("1.5"),
                601
        );

        assertThrows(BusinessException.class, () -> service.createSimulation(invalidRequest));
    }

    @Test
    @DisplayName("Should throw NotFoundException when simulation not found")
    void shouldThrowNotFoundExceptionWhenSimulationNotFound() {
        Long simulationId = 999L;
        when(repository.findById(simulationId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.getSimulation(simulationId));
    }

    @Test
    @DisplayName("Should return simulation when found")
    void shouldReturnSimulationWhenFound() {
        Long simulationId = 1L;
        Simulation simulation = new Simulation();
        when(repository.findById(simulationId)).thenReturn(simulation);
        when(mapper.toResponseDTO(simulation)).thenReturn(new SimulationResponseDTO());

        SimulationResponseDTO response = service.getSimulation(simulationId);

        assertNotNull(response);
    }
}

