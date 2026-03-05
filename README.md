# Veículo API - Tinnova Code Challenge

Uma aplicação Spring Boot para gerenciamento de veículos com autenticação JWT e cache Redis.

## Sobre a Aplicação

Esta é uma API REST que permite gerenciar um catálogo de veículos. A aplicação oferece funcionalidades como:

- **Autenticação e Autorização**: Sistema de login/registro com tokens JWT
- **CRUD de Veículos**: Criar, ler, atualizar e deletar veículos
- **Filtros e Buscas**: Buscar veículos por critérios específicos
- **Cache com Redis**: Melhor performance nas consultas
- **Relatórios**: Gerar relatórios de marcas de veículos
- **Integração com APIs Externas**: Serviço de câmbio para conversão de moedas

## Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Security com JWT
- Spring Data JPA
- Redis
- Docker
- Maven

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Docker (para Redis)

## Configuração e Execução

### 1. Iniciar Redis com Docker

```bash
docker run -d -p 6379:6379 --name redis redis:latest
```

### 2. Compilar a Aplicação

```bash
mvn clean install
```

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Endpoints Principais

### Autenticação
- `POST /auth/login` - Fazer login e obter token JWT
- `POST /auth/register` - Registrar novo usuário

### Veículos
- `GET /veiculos` - Listar todos os veículos
- `POST /veiculos` - Criar novo veículo
- `GET /veiculos/{id}` - Obter detalhes de um veículo
- `PUT /veiculos/{id}` - Atualizar veículo
- `PATCH /veiculos/{id}` - Atualização parcial de veículo
- `DELETE /veiculos/{id}` - Deletar veículo
- `GET /veiculos/relatorios/por-marca` - Gerar relatório por marca

## Variáveis de Ambiente

Configure em `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tinnova_db
    username: root
    password: password
  redis:
    host: localhost
    port: 6379
```

## Documentação da API

A documentação Swagger está disponível em: `http://localhost:8080/swagger`


