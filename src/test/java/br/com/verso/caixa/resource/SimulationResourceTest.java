package br.com.verso.caixa.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@DisplayName("SimulationResource Integration Tests")
class SimulationResourceTest {

    @Test
    @DisplayName("Should create simulation successfully")
    void shouldCreateSimulationSuccessfully() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("valorInicial", equalTo(1000.00f))
                .body("taxaJurosMensal", equalTo(1.5f))
                .body("prazoMeses", equalTo(12))
                .body("valorTotalFinal", notNullValue())
                .body("valorTotalJuros", notNullValue())
                .body("memoriaCalculo", hasSize(12));
    }

    @Test
    @DisplayName("Should return 400 when valorInicial is zero")
    void shouldReturn400WhenValorInicialIsZero() {
        String payload = """
                {
                  "valorInicial": 0,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when valorInicial is negative")
    void shouldReturn400WhenValorInicialIsNegative() {
        String payload = """
                {
                  "valorInicial": -1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when taxaJurosMensal is negative")
    void shouldReturn400WhenTaxaIsNegative() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": -1.5,
                  "prazoMeses": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when prazoMeses is zero")
    void shouldReturn400WhenPrazoIsZero() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 0
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when prazoMeses exceeds 600")
    void shouldReturn400WhenPrazoExceedsLimit() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 601
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 404 when simulation not found")
    void shouldReturn404WhenSimulationNotFound() {
        given()
                .when()
                .get("/api/v1/simulations/99999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should retrieve created simulation successfully")
    void shouldRetrieveCreatedSimulationSuccessfully() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 12
                }
                """;

        Long simulationId = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/v1/simulations/" + simulationId)
                .then()
                .statusCode(200)
                .body("id", equalTo(simulationId.intValue()))
                .body("valorInicial", equalTo(1000.00f))
                .body("memoriaCalculo", hasSize(12));
    }

    @Test
    @DisplayName("Should calculate correct memory for first month")
    void shouldCalculateCorrectMemoryForFirstMonth() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 3
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(201)
                .body("memoriaCalculo[0].mes", equalTo(1))
                .body("memoriaCalculo[0].saldoInicial", equalTo(1000.00f))
                .body("memoriaCalculo[0].jurosMes", equalTo(15.00f))
                .body("memoriaCalculo[0].saldoFinal", equalTo(1015.00f));
    }

    @Test
    @DisplayName("Should calculate correct memory for second month")
    void shouldCalculateCorrectMemoryForSecondMonth() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 3
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(201)
                .body("memoriaCalculo[1].mes", equalTo(2))
                .body("memoriaCalculo[1].saldoInicial", equalTo(1015.00f))
                .body("memoriaCalculo[1].jurosMes", equalTo(15.23f))
                .body("memoriaCalculo[1].saldoFinal", equalTo(1030.23f));
    }

    @Test
    @DisplayName("Should return 400 when required fields are missing")
    void shouldReturn400WhenRequiredFieldsMissing() {
        String payload = """
                {
                  "valorInicial": 1000.00
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when trying to set taxaJurosMensal to 100.01")
    void shouldReturn400WhenTaxaExactlyMoreThan100() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 100.01,
                  "prazoMeses": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should create simulation with maximum allowed taxaJurosMensal")
    void shouldCreateSimulationWithMaximumTaxa() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 100.00,
                  "prazoMeses": 12
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Should create simulation with maximum allowed prazoMeses")
    void shouldCreateSimulationWithMaximumPrazo() {
        String payload = """
                {
                  "valorInicial": 1000.00,
                  "taxaJurosMensal": 1.5,
                  "prazoMeses": 600
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/v1/simulations")
                .then()
                .statusCode(201)
                .body("memoriaCalculo", hasSize(600));
    }
}

