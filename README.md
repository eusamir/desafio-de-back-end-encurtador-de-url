# 🔗 Desafio encurtador de url para a TDS Company.

Projeto de encurtador de URLs desenvolvido em **Java**, com **Spring Boot**, **MongoDB** e **Docker**.

---

## 📌 Sobre o Projeto

Este sistema permite:

- Encurtar URLs longas
- Gerar códigos únicos para redirecionamento
- Acompanhar estatísticas de acesso

Todas as informações são **persistidas em um banco MongoDB**.

---

## 🚀 Tecnologias Utilizadas

- Java 21  
- Spring Boot 3.4.5  
- Spring Web  
- Spring Data MongoDB  
- Spring Validation  
- Lombok  
- Springdoc OpenAPI (Swagger)  
- JUnit e Mockito  
- Docker & Docker Compose  

---

## 🧱 Estrutura do Projeto

```
src/
├── domain/
│   ├── model/
│   │   ├── dto/              # DTOs de entrada e saída
│   │   └── entity/           # Entidades do MongoDB
│   ├── repository/           # Interface do repositório
│   └── service/              # Regras de negócio
├── mapper/                   # Conversores entre DTOs e entidades
├── controller/               # Endpoints REST
└── suport/exception/         # Exceções customizadas
```

---

## 🧠 Lógica de Negócio - `UrlService`

A classe `UrlService` centraliza as regras de negócio.

### Responsabilidades

- ✅ Validação da URL de entrada
- 🔐 Geração de código curto único com `UUID`
- 🏗️ Montagem da URL encurtada usando a base do request
- 💾 Persistência de dados no **MongoDB**
- 🔁 Redirecionamento para a URL original
- 📊 Registro e consulta de estatísticas de acesso

### Principais Métodos

- `generateShortenUrl(request, servletRequest)`  
  Cria e salva uma nova URL encurtada.

- `getFullUrlById(id)`  
  Redireciona para a URL original e atualiza o contador de acessos.

- `getStatsById(id)`  
  Retorna estatísticas da URL.

---

## 💾 Banco de Dados - MongoDB

### Armazena:

- URL original  
- Código curto  
- URL encurtada  
- Quantidade de acessos  
- Datas de criação e último acesso  

### Subindo banco com Docker Compose:

```bash
docker-compose up -d mongodb
```

O MongoDB estará disponível na porta padrão: **27017**.

---

## 📚 Documentação com Swagger

Documentação gerada com **springdoc-openapi**.  
Acesse em:

[🔗 Acessar Swagger UI](http://localhost:8080/swagger-ui.html)

---

## 🧪 Testando a API

### Criar URL encurtada

```http
POST /api/v1/shorten
Content-Type: application/json

{
  "url": "https://www.exemplo.com"
}
```

#### ✅ Resposta de sucesso:

```json
{
  "originalUrl": "https://exemplo.com",
  "shortUrl": "http://localhost:8080/abc123",
  "accessCount": 0,
  "createdAt": "2025-05-17T12:00:00"
}
```

### Redirecionar

```http
GET /api/v1/{codigo}
```
- Retorna **302 Found** com redirecionamento.
- Se o código for inválido:

```json
{
  "status": 404,
  "message": "Not found"
}
```

### Estatísticas

```http
GET /api/v1/{codigo}/stats
```
#### ✅ Resposta de sucesso:

```json
{
  "shortenCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123",
  "fullUrl": "https://exemplo.com",
  "totalAccesses": 20,
  "averagePerDay": 6.6
}
```

---

## 🛠️ Build e Execução

### ✅ Opção 1: Rodar com Docker Compose (recomendado)

Certifique-se de ter o **Docker** e o **Docker Compose** instalados.

```bash
docker-compose up --build
```

A aplicação será exposta em: [http://localhost:8080](http://localhost:8080)

O MongoDB estará disponível internamente no container na porta **27017**.

> 💡 A documentação Swagger estará disponível em:  
> [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### 💻 Opção 2: Rodar localmente (sem Docker)

#### 1. Compilar o projeto:

```bash
./mvnw clean package
```
ou
```bash
mvn clean package
```

#### 2. Subir o MongoDB (se ainda não estiver rodando):

Você pode usar o Docker para subir o MongoDB separadamente:

```bash
docker-compose up -d mongodb
```

#### 3. Executar a aplicação:

```bash
java -jar target/urlshortener-0.0.1-SNAPSHOT.jar
```

---

## 📂 Autor

Desenvolvido por Samir Gomes De Araújo Andrade.
