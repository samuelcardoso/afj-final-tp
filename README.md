[![USER-MS CI](https://github.com/atiliosilfer/afj-final-tp/actions/workflows/action.yml/badge.svg)](https://github.com/atiliosilfer/afj-final-tp/actions/workflows/action.yml)
# TP Final AFJ (Sistema Ecommerce em Java)

Este é um monorepo para um sistema de ecommerce, contendo os seguintes domínios:

- **Usuário**: Responsável por cadastrar o usuário no sistema.
- **Produto**: Responsável por listar os produtos disponíveis.
- **Compra - Produtor**: Responsável por gerar a intenção de compra.
- **Compra - Consumidor**: Responsável por processar a intenção de compra.
- **Estoque**: Processa a baixa no estoque de uma compra.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para criação de microservices.
- **Kotlin**: Linguagem de programação utilizada.
- **MongoDB**: Banco de dados NoSQL utilizado para o serviço de produtos.
- **MySQL**: Banco de dados relacional utilizado para os serviços de compra, estoque e usuário.
- **RabbitMQ**: Mensageria utilizada para comunicação entre os microservices de compra.
- **Docker**: Containerização dos serviços e bancos de dados.
- **Docker Compose**: Orquestração dos containers.

## Microsserviços do Sistema

```plaintext
> products-ms

> purchase-ms

> stock-ms
Alunos:
Bruno Silveira Cerqueira Lima - 208435
Emanuel Borges da Silva - 208978
Isaac de Sousa Lima Azevedo - 213692
Maxel Udson Alves da Conceição - 207144
Vinícius de Oliveira Baldenebro - 210157

> user-ms
```

## Configurações de Banco de Dados

- **MongoDB (products-ms)**:

  - URI: `mongodb://<user>:<pass>@localhost:27017/productsdb`
  - Porta: `27017`

- **MySQL (purchase-ms, stock-ms, user-ms)**:
  - Host: `localhost`
  - Portas:
    - Purchase: `3307`
    - Stock: `3308`
    - User: `3309`

## Configurações de Mensageria

- **RabbitMQ**:
  - Host: `localhost`
  - Porta: `5672`
  - Exchange: `purchaseExchange`
  - Queue: `purchaseQueue`
  - Routing Key: `purchaseRoutingKey`

## Comandos Auxiliares

- Derrubar todos os containers e remover volumes:

  ```bash
  docker-compose down -v
  ```

- Subir os containers:
  ```bash
  docker-compose up --build
  ```

## URLs de Acompanhamento

- **MongoExpress**:

  - URL: [http://localhost:8090](http://localhost:8090)
  - Usuário: `admin`
  - Senha: `pass`

- **MySQL (via phpMyAdmin)**:

  - URL: [http://localhost:8091](http://localhost:8091)

- **RabbitMQ**:
  - URL: [http://localhost:15672](http://localhost:15672)
  - Usuário: `guest`
  - Senha: `guest`

---
## Grupo - Contexto 3: purchase-ms (message producer)

### Integrantes

* 207854 - Irla Paula Andrade Amaral
* 212017 - Jose Carlos Macedo Lacerda
* 208178 - Jose Carlos Pereira Silva Filho
* 207042 - Ryan Souza Camargos de Oliveira
