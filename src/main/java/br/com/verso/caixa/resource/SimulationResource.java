package br.com.verso.caixa.resource;

import br.com.verso.caixa.dto.request.SimulationRequestDTO;
import br.com.verso.caixa.dto.response.SimulationResponseDTO;
import br.com.verso.caixa.service.SimulationService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/v1/simulations")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Simulations", description = "API de simulações financeiras com juros compostos")
public class SimulationResource {

    private final SimulationService service;

    public SimulationResource(SimulationService service) {
        this.service = service;
    }

    @POST
    @Operation(summary = "Criar nova simulação financeira", description = "Cria uma simulação de financiamento com cálculo de juros compostos mês a mês")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Simulação criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimulationResponseDTO.class)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requisição inválida"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    public Response createSimulation(@Valid SimulationRequestDTO request) {
        SimulationResponseDTO response = service.createSimulation(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    //buscar todas as simulações
    @GET
    @Operation(summary = "Listar todas as simulações", description = "Retorna uma lista contendo todas as simulações financeiras calculadas")
    public Response getAllSimulations() {
        List<SimulationResponseDTO> simulations = service.getAllSimulations();
        return Response.ok(simulations).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Consultar simulação", description = "Retorna os detalhes de uma simulação previamente criada")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Simulação encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimulationResponseDTO.class)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Simulação não encontrada"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    public Response getSimulation(@PathParam("id") Long id) {
        SimulationResponseDTO response = service.getSimulation(id);
        return Response.ok(response).build();
    }
}

