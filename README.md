
# 🚀 Forum Hub API

API RESTful para gerenciamento de tópicos, usuários e respostas, simulando um fórum online.  
Desenvolvida com **Spring Boot** como parte do desafio da **Alura/ONE**.

---

## ✨ Funcionalidades

- 🔐 Autenticação com **JWT**
- 👥 CRUD completo de **Usuários**, **Tópicos** e **Respostas**
- ✅ **Validações** com Jakarta Validation
- 📄 **Documentação Interativa** com Swagger UI
- 🔎 Design RESTful com boas práticas

---

## 🛠️ Tecnologias Utilizadas

- ⚙️ **Java 17**, **Spring Boot 3.x**
- 🛡 **Spring Security + JWT**
- 🗃 **MySQL**, **JPA/Hibernate**, **Flyway**
- 📦 **Maven**, **Lombok**
- 🧪 **SpringDoc OpenAPI** (Swagger)

---

## 🚀 Como Executar Localmente

### ✅ Pré-requisitos

- Java 17+
- MySQL 8+
- Maven
- Git

---

### 📦 1. Clone o Projeto

```bash
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
cd SEU_REPOSITORIO
```

---

### 🗄️ 2. Crie o Banco de Dados MySQL

```sql
CREATE DATABASE forum_hub;
```

> O Flyway cuidará da criação automática das tabelas ao iniciar a aplicação.

---

### 🔐 3. Configure as Variáveis de Ambiente

Crie o arquivo `src/main/resources/application-local.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forum_hub
spring.datasource.username=root
spring.datasource.password=SUA_SENHA
spring.flyway.url=jdbc:mysql://localhost:3306/forum_hub
spring.flyway.user=root
spring.flyway.password=SUA_SENHA
api.security.token.secret=SUA_CHAVE_SECRETA
```

> ❗ **Importante:** Não versionar esse arquivo. Confirme que ele está no `.gitignore`.

---

### ▶️ 4. Execute a Aplicação

**Via terminal (Maven):**

```bash
mvn spring-boot:run
```

Ou via IntelliJ: clique com o botão direito na classe `ApitopicosApplication.java` e selecione `Run`.

> A aplicação será iniciada por padrão na porta: `http://localhost:8080`

---

## 📚 Documentação da API

Acesse o Swagger UI:

👉 [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

### 🔑 Para usar endpoints protegidos:

1. Cadastre um usuário (`POST /usuarios`)
2. Faça login (`POST /login`) e receba o token JWT
3. Clique no botão **Authorize** no Swagger UI e use:
   ```
   Bearer SEU_TOKEN_AQUI
   ```

---

## 🧪 Exemplos de Código

> ✅ Trechos reduzidos e representativos. Consulte os arquivos completos no projeto para mais detalhes.

### 🧩 Exemplo de Controller Java

```java
// HelloController.java
@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public String hello() {
        return "Hello, Forum Hub!";
    }
}
```

### 🗃 SQL - Exemplo de criação de tabela

```sql
-- V1__create-table-topicos.sql
CREATE TABLE topicos (
  id BIGINT AUTO_INCREMENT,
  titulo VARCHAR(255) NOT NULL,
  mensagem VARCHAR(1000) NOT NULL,
  data_criacao DATETIME NOT NULL,
  estado_topico VARCHAR(50) NOT NULL,
  autor VARCHAR(100) NOT NULL,
  curso VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);
```

### ⚙️ Configuração de ambiente local

```properties
# application-local.properties
spring.datasource.url=jdbc:mysql://localhost:3306/forum_hub
spring.datasource.username=root
spring.datasource.password=SUA_SENHA
```

---

## 📂 Organização dos Arquivos

| Componente       | Caminho                                                           |
|------------------|-------------------------------------------------------------------|
| Configuração App | `src/main/resources/application.properties`                      |
| Entidade Usuário | `src/main/java/com/forum/hub/apitopicos/usuario/Usuario.java`    |
| Repositório      | `src/main/java/com/forum/hub/apitopicos/topico/TopicoRepository.java` |
| Controller Exemplo | `src/main/java/com/forum/hub/apitopicos/controller/HelloController.java` |

---

## 🤝 Contribuindo

Contribuições são bem-vindas!

1. Abra uma **issue** com sugestões ou problemas
2. Envie um **pull request** com melhorias

---

## 🙏 Agradecimentos

Projeto desenvolvido durante o programa **ONE - Oracle Next Education** em parceria com a **Alura**.

> Aprendizado prático com foco em **APIs RESTful, segurança, boas práticas e versionamento de banco de dados**.

---
