# 📘 Maintenance Control API

## 🛠️ Descrição

A **Maintenance Control API** é uma aplicação `RESTful` desenvolvida em Java 17 com Spring Boot 3.5, projetada para gerenciar ordens de serviço de manutenção de máquinas em um ambiente industrial. A aplicação permite criar, listar, atualizar e deletar máquinas e ordens de serviço, bem como consultar ordens por status ou máquina.

## 🚀 Principais Funcionalidades

- ✅ Cadastro e atualização de máquinas
- ✅ Cadastro e atualização de ordens de serviço
- ✅ Cadastro e atualização de Técnicos
- ✅ Consulta de ordens por:
  - 🔍 ID
  - 🔍 Status da ordem
  - 🔍 Máquina associada
  - 🔍 Técnicos associados
- ✅ Alteração de status das ordens 
- ✅ Exclusão de máquinas, ordens de serviço e Técnicos
- ✅ Consulta de máquinas e técnicos por:
  - 🔍 ID
  - 🔍 Listar maquinas e técnicos

## 🏗️ Tecnologias Utilizadas

* Java 17

* Spring Boot 3.5

* Spring Data JPA

* Spring Validation

* MySQL (produção)

* Lombok

* JUnit 5

* Mockito
  
* H2 para testes de Controller

* MockMvc para testes de Controller

* Swagger/OpenAPI 3 (documentação automática)

* Jacoco (análise de cobertura de testes)

## 📂 Estrutura do Projeto

```plaintext

src/
└── main/
    ├── java/com/everton/maintenance_control/
    │   ├── controllers/          # Controllers REST
    │   ├── dtos/                # DTOs de entrada e saída
    │   ├── enums/               # Enums para status de máquina e ordem
    │   ├── exceptions/          # Custom exceptions e handlers globais
    │   ├── model/               # Entidades JPA (Machine, ServiceOrder, etc)
    │   ├── repository/          # Interfaces JPA Repository
    │   └── services/            # Lógica de negócios (Services)
    │
    └── resources/
        ├── application.yml      # Configurações do Spring Boot
        └── static/              # (opcional) arquivos estáticos
```

## ⚙️ Configuração Local

 **1. Clone o repositório:**

 ```bash

 git clone https://github.com/seu-usuario/maintenance-control.git
cd maintenance-control

```

**2. Configure o banco de dados no application.yml:**

```bash
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/maintenance_control
    username: root
    password: sua_senha

```

**3. Compile e rode a aplicação:**
```bash

./mvnw clean install
./mvnw spring-boot:run
```

**4. Acesse o Swagger (Documentação de API):**
```bash

http://localhost:8080/swagger-ui.html

```

## ✅ Testes

A aplicação possui cobertura de testes unitários e de integração:

* **Mockito** para simular dependências.

* **MockMvc** para testar endpoints dos controllers.

* **Jacoco** para verificar a cobertura de testes.

Executar os testes:

```bash

./mvnw clean test
```

Após a execução, a cobertura pode ser vista no:
```bash

target/site/jacoco/index.html

```

## 📌 Principais Endpoints

| Método   | Endpoint                                | Descrição                            |
| -------- | --------------------------------------- | ------------------------------------ |
| `POST`   | `/machine`                              | Cadastrar nova máquina               |
| `PUT`    | `/machine/{id}`                         | Atualizar máquina existente          |
| `DELETE` | `/machine/{id}`                         | Deletar máquina                      |
| `GET`    | `/machine`                              | Listar todas as máquinas             |
| `POST`   | `/serviceOrder`                         | Cadastrar nova ordem de serviço      |
| `PATCH`  | `/serviceOrder/{id}`                    | Iniciar execução de ordem de serviço |
| `GET`    | `/serviceOrder/order/{idMachine}`       | Listar ordens por Maquina            |
| `GET`    | `/serviceOrder/order/{idTech}`          | Listar ordens por técnico            |
| `GET`    | `/serviceOrder/statusOrder?status=OPEN` | Listar ordens por status             |
| `DELETE` | `/serviceOrder/{id}`                    | Deletar ordem de serviço             |
| `POST`   | `/technician`                           | Cadastrar Técnico                    |
| `PUT`    | `/technician/{id}`                      | Atualizar máquina existente          |
| `DELETE` | `//technician/{id}`                     | Deletar técnico                      |
| `GET`    | `//technician`                          | Listar todas as técnicos             |

## 👨‍💻 Autor
**Everton Silva**

Java Developer | Backend Enthusiast | Industrial Systems Integration







