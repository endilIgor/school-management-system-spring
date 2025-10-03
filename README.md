# School Management System

Sistema completo de gestão escolar desenvolvido com Spring Boot 3.5.6 e Java 21.

## Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Regras do Sistema Educacional](#regras-do-sistema-educacional)
- [Configuração e Instalação](#configuração-e-instalação)
- [Documentação](#documentação)
- [API Endpoints](#api-endpoints)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)

---

## Visão Geral

O School Management System é uma aplicação REST API completa para gerenciamento de instituições de ensino, oferecendo controle de:

- **Usuários** (Administradores, Professores, Alunos, Responsáveis)
- **Matrículas** e Transferências de Alunos
- **Turmas** e Salas de Aula
- **Disciplinas** e Professores
- **Notas** e Avaliações
- **Frequência** e Presença
- **Boletins** e Histórico Acadêmico
- **Relatórios** Gerenciais
- **Eventos** Escolares

---

## Tecnologias

### Backend
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM

### Banco de Dados
- **H2 Database** (desenvolvimento)
- Suporte para MySQL/PostgreSQL (produção)

### Documentação
- **SpringDoc OpenAPI 3** (Swagger UI)

### Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocks para testes unitários
- **JaCoCo** - Cobertura de código (meta: 85%)
- **Spring Boot Test** - Testes de integração

### Ferramentas
- **Lombok** - Redução de boilerplate
- **Maven** - Gerenciamento de dependências

---

## Funcionalidades

### Gerenciamento de Usuários
- ✅ Cadastro de usuários com validação de CPF
- ✅ Perfis de acesso (Admin, Teacher, Student)
- ✅ Autenticação e autorização
- ✅ Gestão de dados cadastrais

### Gestão Acadêmica
- ✅ Matrícula de alunos
- ✅ Criação e gestão de turmas
- ✅ Controle de capacidade de salas
- ✅ Transferência entre turmas
- ✅ Atribuição de professores às disciplinas

### Notas e Avaliações
- ✅ Registro de notas (escala 0-10)
- ✅ Múltiplos tipos de avaliação (Prova, Trabalho, Teste, etc.)
- ✅ Cálculo automático de médias
- ✅ Sistema de bimestres (4 quarters)
- ✅ Geração de boletins

### Frequência
- ✅ Registro diário de presença/falta
- ✅ Justificativa de ausências
- ✅ Cálculo automático de percentual de frequência
- ✅ Alertas para frequência abaixo de 75%

### Relatórios
- ✅ Relatório de notas por turma
- ✅ Relatório de frequência
- ✅ Relatório de aprovação/reprovação
- ✅ Relatório de evasão escolar
- ✅ Exportação CSV/Excel

### Eventos Escolares
- ✅ Calendário de eventos
- ✅ Reuniões de pais
- ✅ Feriados e recessos
- ✅ Datas de provas

---

## Regras do Sistema Educacional

### Estrutura Acadêmica

#### Ano Letivo
- Dividido em **4 bimestres**
- Cada bimestre possui notas e frequência independentes
- Sistema de recuperação por bimestre

#### Turmas
- **Capacidade máxima** configurável
- Bloqueio automático de matrículas quando lotada
- Controle de turno (Manhã, Tarde, Noite)
- Ano letivo específico (ex: 2025)

#### Número de Matrícula
- **Formato:** YYYY###### (ano + 6 dígitos)
- **Exemplo:** 2025000001
- Único e permanente para cada aluno
- Gerado no ato da matrícula

### Sistema de Avaliação

#### Escala de Notas
- **Mínima:** 0.0
- **Máxima:** 10.0
- Precisão de duas casas decimais

#### Tipos de Avaliação
- **EXAM** - Prova
- **ASSIGNMENT** - Trabalho
- **TEST** - Teste
- **PARTICIPATION** - Participação
- **PROJECT** - Projeto
- **OTHER** - Outros

#### Cálculo de Médias
```
Média do Bimestre = Soma de todas as notas / Quantidade de avaliações
Média Final = (Média 1º Bim + Média 2º Bim + Média 3º Bim + Média 4º Bim) / 4
```

### Critérios de Aprovação

#### Por Nota
- **≥ 7.0:** APROVADO
- **5.0 a 6.9:** RECUPERAÇÃO
- **< 5.0:** REPROVADO

#### Por Frequência
- **≥ 75%:** Frequência adequada
- **< 75%:** REPROVADO por falta (independente da nota)

#### Status do Boletim
- **APPROVED:** Aprovado em todas as disciplinas
- **RECOVERY:** Necessita recuperação em uma ou mais disciplinas
- **FAILED:** Reprovado por nota ou frequência
- **STUDYING:** Ano letivo em andamento

### Recuperação

#### Regras
- Disponível para alunos com média entre 5.0 e 6.9
- Pode ser oferecida por bimestre ou final
- Nova nota substitui ou calcula nova média conforme critério da escola

### Frequência

#### Presença/Falta
- Registro obrigatório por aula
- Professor responsável pelo lançamento
- Faltas podem ser justificadas posteriormente

#### Justificativas Aceitas
- Atestado médico
- Declaração de comparecimento
- Eventos oficiais
- Outros conforme regimento escolar

#### Cálculo
```
Percentual de Frequência = (Total de Presenças / Total de Aulas) × 100
```

### Transferências

#### Regras
- Permitida entre turmas da mesma instituição
- Turma destino deve ter vagas disponíveis
- Mantém histórico acadêmico completo
- Atualiza ocupação das turmas automaticamente

### Validações Automáticas

#### CPF
- Deve ser válido segundo algoritmo oficial
- Único no sistema
- Não pode ser alterado após cadastro

#### Email
- Formato válido
- Único por usuário

#### Número de Matrícula
- Formato YYYY######
- Ano deve estar entre 1900 e ano atual
- Único no sistema
- Não pode ser alterado

#### Senha
- Mínimo 8 caracteres
- Armazenada com hash (segurança)

#### Telefone
- Entre 10 e 15 dígitos
- Formato brasileiro aceito

---

## Configuração e Instalação

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Instalação

1. **Clone o repositório**
```bash
git clone <repository-url>
cd school-management-system-spring
```

2. **Configure o banco de dados** (opcional)

O sistema usa H2 por padrão. Para produção, edite `application.properties`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/school_db
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/school_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

3. **Compile o projeto**
```bash
./mvnw clean compile
```

4. **Execute os testes**
```bash
./mvnw test
```

5. **Inicie a aplicação**
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Acessar Documentação da API

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

### Console H2 (Desenvolvimento)

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** jdbc:h2:mem:testdb
- **Username:** sa
- **Password:** (em branco)

---

## Documentação

### Manuais de Usuário

- 📘 [**Manual do Administrador**](docs/ADMIN_MANUAL.md)
  - Gerenciamento completo do sistema
  - Cadastros, relatórios e configurações

- 📗 [**Manual do Professor**](docs/TEACHER_MANUAL.md)
  - Lançamento de notas e frequência
  - Geração de boletins
  - Acompanhamento de turmas

- 📙 [**Manual do Responsável**](docs/GUARDIAN_MANUAL.md)
  - Acompanhamento de alunos
  - Consulta de notas e frequência
  - Visualização de boletins

### Documentação Técnica

- **API Reference:** Swagger UI (http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec:** JSON format (http://localhost:8080/v3/api-docs)

---

## API Endpoints

### Principais Recursos

#### Students (Alunos)
```
POST   /student                          - Matricular aluno
GET    /student/id/{id}                  - Buscar por ID
GET    /student/enrollment/{number}      - Buscar por matrícula
GET    /student/classroom/{id}           - Listar por turma
GET    /student/academic-history/{number}- Histórico completo
PUT    /student/{number}                 - Atualizar dados
PUT    /student/{number}/transfer/{id}   - Transferir turma
```

#### Grades (Notas)
```
POST   /grade                            - Registrar nota
GET    /grade/student/{id}               - Notas do aluno
GET    /grade/average                    - Calcular média
PUT    /grade/{id}                       - Atualizar nota
GET    /grade/report-card/{id}/{quarter} - Gerar boletim
```

#### Attendance (Frequência)
```
POST   /attendance                       - Registrar presença
GET    /attendance/student/{id}          - Frequência do aluno
GET    /attendance/classroom/{id}/date   - Lista de chamada
PUT    /attendance/{id}/justify          - Justificar falta
GET    /attendance/calculate/{id}        - Calcular percentual
```

#### Classrooms (Turmas)
```
POST   /classroom                        - Criar turma
GET    /classroom/all                    - Listar todas
GET    /classroom/{id}                   - Buscar por ID
PUT    /classroom/{id}                   - Atualizar
```

#### Reports (Relatórios)
```
GET    /report/grades/classroom/{id}     - Notas da turma
GET    /report/attendance/classroom/{id} - Frequência da turma
GET    /report/approval                  - Relatório de aprovação
GET    /report/dropout                   - Relatório de evasão
GET    /report/export/csv/{id}           - Exportar CSV
GET    /report/export/excel/{id}         - Exportar Excel
```

---

## Testes

### Executar Testes

```bash
# Todos os testes
./mvnw test

# Apenas testes unitários
./mvnw test -Dtest=*ServiceTest

# Apenas testes de integração
./mvnw test -Dtest=*IntegrationTest

# Com relatório de cobertura
./mvnw clean test jacoco:report
```

### Cobertura de Código

Após executar os testes com JaCoCo:

```bash
# Visualizar relatório
open target/site/jacoco/index.html
```

**Meta de Cobertura:** 85%

### Tipos de Testes

#### Testes Unitários (36 testes)
- `StudentServiceTest` - 9 testes
- `GradeServiceTest` - 9 testes
- `AttendanceServiceTest` - 10 testes
- `ReportCardServiceTest` - 8 testes

#### Testes de Repositório (12 testes)
- `StudentRepositoryTest` - 6 testes
- `GradeRepositoryTest` - 6 testes

#### Testes de Integração (17 testes)
- `StudentEnrollmentIntegrationTest` - 6 testes
- `GradesReportCardIntegrationTest` - 6 testes
- `UserRoleIntegrationTest` - 5 testes

**Total:** 65+ testes

---

## Estrutura do Projeto

```
school-management-system-spring/
├── src/
│   ├── main/
│   │   ├── java/com/nagi/school_management_system_spring/
│   │   │   ├── config/          # Configurações (Swagger, etc)
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exception Handlers
│   │   │   ├── mapper/          # Entity ↔ DTO Mappers
│   │   │   ├── model/           # Entidades JPA
│   │   │   │   └── enums/       # Enumerações
│   │   │   ├── repository/      # Repositórios JPA
│   │   │   ├── service/         # Lógica de Negócio
│   │   │   └── validation/      # Validadores Customizados
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/nagi/school_management_system_spring/
│           ├── integration/     # Testes de Integração
│           ├── repository/      # Testes de Repositório
│           ├── service/         # Testes Unitários
│           └── util/            # Utilitários de Teste
├── docs/                        # Documentação
│   ├── ADMIN_MANUAL.md
│   ├── TEACHER_MANUAL.md
│   └── GUARDIAN_MANUAL.md
├── pom.xml
└── README.md
```

---

## Validações Customizadas

O sistema inclui validadores customizados para:

### ValidCPF
Valida CPF brasileiro com dígitos verificadores

### ValidEnrollmentNumber
Valida formato YYYY###### e verifica ano válido

### Outras Validações
- Email único
- Telefone no formato correto
- Datas válidas
- Notas entre 0 e 10

---

## Tratamento de Exceções

### Exceções Customizadas

- `ResourceNotFoundException` - Recurso não encontrado
- `StudentAlreadyEnrolledException` - Matrícula duplicada
- `ClassroomFullException` - Turma lotada
- `InvalidGradeException` - Nota inválida
- `InsufficientAttendanceException` - Frequência insuficiente

### GlobalExceptionHandler

Captura todas as exceções e retorna respostas padronizadas:

```json
{
  "timestamp": "2025-10-03T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 123",
  "path": "/student/id/123"
}
```

---

## Contribuindo

### Code Standards

- Código, variáveis e métodos em **inglês**
- Commits em **inglês**
- Documentação em **português**
- Seguir convenções Java (camelCase, PascalCase)
- Sem emojis em código ou commits

### Padrão de Commits

```
feat: add new feature
fix: correct bug
refactor: improve code structure
docs: update documentation
test: add tests
```

---

## Licença

MIT License

---

## Suporte

- **Email:** support@schoolmanagement.com
- **Issues:** GitHub Issues
- **Documentation:** Swagger UI

---

## Roadmap

### Versão 2.0 (Planejado)
- [ ] Autenticação JWT
- [ ] Sistema de permissões granular
- [ ] Notificações por email/SMS
- [ ] Dashboard com gráficos
- [ ] App mobile
- [ ] Integração com sistemas de pagamento
- [ ] Sistema de mensagens internas
- [ ] Biblioteca virtual
- [ ] Gestão de transporte escolar

---

## Autores

- **Nagiitoshi** - Desenvolvimento Inicial

---

**Desenvolvido com Spring Boot 3.5.6 e Java 21**
