


## Objetivo do Projeto

Construir uma API REST profissional utilizando Java 25, Quarkus e H2 Database para simulação de financiamentos e investimentos com juros compostos.

A API deve:

* Receber parâmetros financeiros
* Calcular juros compostos com precisão financeira
* Gerar memória de cálculo mensal
* Persistir os dados
* Permitir consulta posterior
* Possuir documentação OpenAPI automática
* Possuir cobertura mínima de testes de 80%
* Executar totalmente sem Docker

---

# Stack Obrigatória

## Linguagem

* Java 25

## Framework

* Quarkus

## Banco de Dados

* H2 Database Embedded

## Build Tool

* Maven

## Bibliotecas Obrigatórias

* Hibernate ORM Panache
* RESTEasy Reactive
* Jackson
* Bean Validation
* SmallRye OpenAPI
* JUnit 5
* Mockito
* RestAssured
* Jacoco

---

# Regras Críticas

## NÃO utilizar

* Docker
* Docker Compose
* double
* float
* regras de negócio no controller
* entidades diretamente nos endpoints

---

# Precisão Financeira

Toda manipulação monetária DEVE utilizar BigDecimal.

NUNCA utilizar double ou float.

## Configuração padrão obrigatória

```java
private static final int SCALE = 2;
private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
```

---

# Arquitetura Obrigatória

O projeto deve seguir rigorosamente:

```text
Resource -> Service -> Repository -> Database
```

## Responsabilidades

### Resource

Responsável apenas por:

* receber requisições
* validar DTOs
* retornar responses HTTP

### Service

Responsável por:

* regras de negócio
* cálculos financeiros
* orquestração
* validações de domínio

### Repository

Responsável apenas por persistência.

### DTO

Responsável pelo contrato da API.

### Entity

Responsável pela persistência JPA.

---

# Estrutura de Pastas Obrigatória

```text
src/main/java/com/github/user/financeapi
│
├── config
│
├── resource
│   └── SimulationResource.java
│
├── service
│   └── SimulationService.java
│
├── repository
│   └── SimulationRepository.java
│
├── entity
│   ├── Simulation.java
│   └── CalculationMemory.java
│
├── dto
│   ├── request
│   │   └── SimulationRequestDTO.java
│   │
│   └── response
│       ├── SimulationResponseDTO.java
│       └── CalculationMemoryDTO.java
│
├── mapper
│   └── SimulationMapper.java
│
├── validation
│
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── BusinessException.java
│   └── NotFoundException.java
│
└── util
```

---

# Endpoints Obrigatórios

# POST /api/v1/simulations

## Objetivo

Criar uma nova simulação financeira.

## Request

```json
{
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12
}
```

## Response HTTP

```http
201 Created
```

## Response Body

```json
{
  "id": 1,
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12,
  "valorTotalFinal": 1195.62,
  "valorTotalJuros": 195.62,
  "memoriaCalculo": [
    {
      "mes": 1,
      "saldoInicial": 1000.00,
      "jurosMes": 15.00,
      "saldoFinal": 1015.00
    }
  ]
}
```

---

# GET /api/v1/simulations/{id}

## Objetivo

Consultar simulação previamente salva.

## Response HTTP

```http
200 OK
```

## Quando não encontrar

```http
404 Not Found
```

---

# Fórmula Obrigatória

Utilizar juros compostos:

```text
M = P(1 + i)^n
```

Onde:

* M = montante final
* P = principal
* i = taxa
* n = prazo

---

# Estratégia Obrigatória de Cálculo

NÃO utilizar apenas fórmula matemática final.

DEVE gerar memória mês a mês.

## Exemplo obrigatório

```java
BigDecimal saldoAtual = valorInicial;

for (int mes = 1; mes <= prazoMeses; mes++) {

    BigDecimal jurosMes = saldoAtual
        .multiply(taxaMensal)
        .divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);

    BigDecimal saldoFinal = saldoAtual.add(jurosMes);

    saldoAtual = saldoFinal;
}
```

---

# Entidades Obrigatórias

# Simulation

Campos obrigatórios:

```java
Long id;
BigDecimal valorInicial;
BigDecimal taxaJurosMensal;
Integer prazoMeses;
BigDecimal valorTotalFinal;
BigDecimal valorTotalJuros;
LocalDateTime dataCriacao;
List<CalculationMemory> memories;
```

---

# CalculationMemory

Campos obrigatórios:

```java
Long id;
Integer mes;
BigDecimal saldoInicial;
BigDecimal jurosMes;
BigDecimal saldoFinal;
Simulation simulation;
```

---

# Relacionamentos Obrigatórios

```java
@OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL)
private List<CalculationMemory> memories;
```

---

# Regras de Validação

## DTO obrigatório

```java
@NotNull
@Positive
private BigDecimal valorInicial;

@NotNull
@Positive
private BigDecimal taxaJurosMensal;

@NotNull
@Positive
private Integer prazoMeses;
```

---

# Regras Extras

Implementar validações adicionais:

* valorInicial > 0
* taxaJurosMensal > 0
* taxaJurosMensal <= 100
* prazoMeses > 0
* prazoMeses <= 600

---

# Tratamento Global de Exceções

Criar handler global.

## Formato obrigatório

```json
{
  "timestamp": "2026-05-19T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "valorInicial deve ser maior que zero",
  "path": "/api/v1/simulations"
}
```

---

# OpenAPI / Swagger

Adicionar:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
```

## Swagger UI

Disponível em:

```text
/swagger-ui
```

---

# Documentação dos Endpoints

Utilizar:

```java
@Operation
@ApiResponse
@Schema
```

---

# DTOs

NUNCA retornar entidades diretamente.

Sempre utilizar DTOs.

---

# Mapper

Criar mapper dedicado.

Preferência:

* mapper manual

Evitar lógica de transformação no Resource.

---

# Repository

Utilizar:

```java
@ApplicationScoped
```

Extender:

```java
PanacheRepository
```

---

# Transações

Métodos de persistência DEVEM utilizar:

```java
@Transactional
```

---

# Configuração do H2

## application.properties

```properties
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:file:./data/finance-db
quarkus.datasource.username=sa
quarkus.datasource.password=sa

quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true
```

---

# Ambiente de Testes

Utilizar banco em memória:

```properties
jdbc:h2:mem:testdb
```

---

# Cobertura de Testes

## Obrigatório

Cobertura mínima:

```text
80%
```

## Meta Ideal

```text
90%+
```

---

# Estratégia de Testes

# Testes Unitários

Cobrir:

* cálculo de juros
* arredondamento
* regras de negócio
* validações
* exceções
* edge cases

---

# Testes de Integração

Cobrir:

* POST
* GET
* serialização
* persistência
* responses HTTP
* cenários inválidos

---

# Casos de Teste Obrigatórios

## Cenários válidos

* cálculo simples
* juros altos
* juros baixos
* prazo longo

## Cenários inválidos

* valor negativo
* taxa negativa
* prazo zero
* payload vazio
* ID inexistente

---

# Jacoco

Obrigatório gerar:

```text
target/site/jacoco/index.html
```

## Plugin obrigatório

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
</plugin>
```

---

# Clean Code

Seguir:

* SOLID
* SRP
* Clean Architecture
* alta coesão
* baixo acoplamento

---

# Convenções

## Classes

PascalCase

## Métodos

camelCase

## Constantes

UPPER_CASE

---

# Logs

Adicionar logs relevantes.

Registrar:

* criação de simulação
* falhas
* consultas
* erros críticos

---

# Serialização

Evitar loops infinitos.

Preferência:

* DTOs isolados

---

# Performance

Evitar:

* N+1
* carregamentos desnecessários

Utilizar:

```java
fetch = FetchType.LAZY
```

---

# Versionamento da API

Obrigatório:

```text
/api/v1
```

---

# README.md Obrigatório

O README deve conter:

* descrição do projeto
* arquitetura
* stack utilizada
* requisitos
* como executar
* como rodar testes
* como validar cobertura
* endpoints
* exemplos JSON
* swagger
* decisões técnicas

---

# Comandos Obrigatórios

## Executar aplicação

```bash
./mvnw quarkus:dev
```

## Executar testes

```bash
./mvnw test
```

## Gerar cobertura

```bash
./mvnw clean verify
```

---

# Dependências Recomendadas

```xml
quarkus-resteasy-reactive-jackson
quarkus-hibernate-orm-panache
quarkus-jdbc-h2
quarkus-smallrye-openapi
quarkus-junit5
rest-assured
jacoco-maven-plugin
```

---

# Objetivo Final

O projeto gerado deve:

* compilar sem erros
* executar sem Docker
* respeitar arquitetura limpa
* utilizar BigDecimal corretamente
* possuir testes robustos
* atingir 80%+ de cobertura
* possuir OpenAPI funcional
* demonstrar qualidade de código de nível pleno/sênior backend

---

# Requisitos de Qualidade

O código deve parecer código de produção.

Evitar:

* métodos gigantes
* lógica procedural
* duplicação
* controllers inchados
* comentários inúteis
* nomes genéricos

---

# Padrão Esperado

O projeto final deve demonstrar domínio de:

* Java moderno
* Quarkus
* REST APIs
* JPA/Hibernate
* testes automatizados
* arquitetura backend
* clean code
* engenharia de software
* precisão financeira
