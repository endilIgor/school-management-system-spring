# Manual do Administrador - Sistema de Gestão Escolar

## Sumário
1. [Visão Geral](#visão-geral)
2. [Gerenciamento de Usuários](#gerenciamento-de-usuários)
3. [Gerenciamento de Turmas](#gerenciamento-de-turmas)
4. [Gerenciamento de Estudantes](#gerenciamento-de-estudantes)
5. [Gerenciamento de Professores](#gerenciamento-de-professores)
6. [Gerenciamento de Disciplinas](#gerenciamento-de-disciplinas)
7. [Relatórios Gerenciais](#relatórios-gerenciais)
8. [Configurações do Sistema](#configurações-do-sistema)

---

## Visão Geral

Como administrador do sistema, você tem acesso completo a todas as funcionalidades do sistema de gestão escolar. Este manual descreve suas responsabilidades e como utilizar cada módulo do sistema.

### Responsabilidades do Administrador

- Cadastro e gerenciamento de usuários (professores, alunos, responsáveis)
- Criação e configuração de turmas
- Matrícula e transferência de alunos
- Atribuição de professores às disciplinas e turmas
- Geração de relatórios gerenciais
- Configuração de períodos letivos (bimestres)
- Gestão de eventos escolares

---

## Gerenciamento de Usuários

### Criar Novo Usuário

**Endpoint:** `POST /user`

**Dados Necessários:**
```json
{
  "name": "Nome Completo",
  "email": "email@exemplo.com",
  "password": "senha123456",
  "cpf": "12345678909",
  "phoneNumber": "11987654321",
  "address": "Rua Exemplo, 123",
  "birthDate": "2000-01-01",
  "usertype": "ADMIN | TEACHER | STUDENT",
  "isActive": true
}
```

**Validações:**
- CPF deve ser válido (11 dígitos)
- Senha deve ter no mínimo 8 caracteres
- Email deve ser único no sistema
- Data de nascimento deve ser anterior à data atual

### Listar Todos os Usuários

**Endpoint:** `GET /user/all`

Retorna lista completa de usuários cadastrados com seus respectivos perfis.

### Atualizar Dados de Usuário

**Endpoint:** `PUT /user/{id}`

Permite atualizar informações do usuário. Não é permitido alterar CPF após cadastro.

### Desativar Usuário

**Endpoint:** `PUT /user/{id}`

Defina `isActive: false` para desativar um usuário sem excluí-lo do sistema.

---

## Gerenciamento de Turmas

### Criar Nova Turma

**Endpoint:** `POST /classroom`

**Dados Necessários:**
```json
{
  "name": "9º Ano A",
  "grade": "9º Ano",
  "shift": "MORNING | AFTERNOON | EVENING",
  "room": "Sala 101",
  "maxCapacity": 35,
  "schoolYear": "2025",
  "isActive": true
}
```

**Importante:**
- `maxCapacity`: Define o número máximo de alunos na turma
- `shift`: Turno da turma (Manhã, Tarde ou Noite)
- `schoolYear`: Ano letivo no formato YYYY

### Atribuir Professor Regente

**Endpoint:** `PUT /classroom/{id}`

Atribua um professor como regente da turma incluindo `homeRoomTeacherId` nos dados.

### Listar Turmas

**Endpoint:** `GET /classroom/all`

Retorna todas as turmas cadastradas com informações de ocupação.

### Consultar Disponibilidade

**Endpoint:** `GET /classroom/{id}`

Mostra:
- Número de alunos matriculados
- Vagas disponíveis
- Professor regente
- Disciplinas atribuídas

---

## Gerenciamento de Estudantes

### Matricular Novo Aluno

**Endpoint:** `POST /student`

**Processo de Matrícula:**

1. **Criar usuário do aluno** (se ainda não existe)
2. **Cadastrar responsável** (opcional)
3. **Efetuar matrícula**

```json
{
  "userId": 1,
  "enrollmentNumber": "2025000001",
  "classroomId": 1,
  "guardianId": 1,
  "status": "ACTIVE",
  "observations": "Observações gerais"
}
```

**Número de Matrícula:**
- Formato: YYYY###### (ano + 6 dígitos sequenciais)
- Exemplo: 2025000001
- Deve ser único no sistema

**Status Disponíveis:**
- `ACTIVE`: Aluno ativo e frequentando
- `TRANSFERRED`: Aluno transferido para outra escola
- `DROPPED_OUT`: Aluno evadido

### Transferir Aluno Entre Turmas

**Endpoint:** `PUT /student/{enrollmentNumber}/transfer/{classroomId}`

**Validações Automáticas:**
- Verifica se a turma de destino tem vagas disponíveis
- Mantém histórico acadêmico do aluno
- Atualiza automaticamente a ocupação das turmas

### Buscar Histórico Acadêmico

**Endpoint:** `GET /student/academic-history/{enrollmentNumber}`

**Retorna:**
- Dados cadastrais do aluno
- Todas as notas por disciplina e bimestre
- Registro completo de frequência
- Boletins gerados

### Listar Alunos por Turma

**Endpoint:** `GET /student/classroom/{classroomId}`

Útil para:
- Verificar lista de chamada
- Consultar ocupação da turma
- Exportar listas

---

## Gerenciamento de Professores

### Cadastrar Professor

**Endpoint:** `POST /teacher`

```json
{
  "userId": 1,
  "specialization": "Matemática",
  "qualification": "Licenciatura em Matemática"
}
```

### Atribuir Disciplina ao Professor

**Endpoint:** `POST /classroom-subject-teacher`

```json
{
  "classroomId": 1,
  "subjectId": 1,
  "teacherId": 1
}
```

**Regras:**
- Um professor pode lecionar várias disciplinas
- Uma disciplina pode ter diferentes professores em turmas diferentes
- Verifique a carga horária do professor antes de atribuir novas disciplinas

### Consultar Carga Horária

**Endpoint:** `GET /teacher/{id}/schedule`

Mostra todas as turmas e disciplinas atribuídas ao professor.

---

## Gerenciamento de Disciplinas

### Criar Nova Disciplina

**Endpoint:** `POST /subject`

```json
{
  "subjectName": "Matemática",
  "workload": 80,
  "description": "Matemática do Ensino Fundamental II"
}
```

**workload:** Carga horária anual em horas/aula

### Listar Disciplinas

**Endpoint:** `GET /subject/all`

### Atualizar Disciplina

**Endpoint:** `PUT /subject/{id}`

---

## Relatórios Gerenciais

### Relatório de Notas por Turma

**Endpoint:** `GET /report/grades/classroom/{classroomId}`

**Informações:**
- Lista de todos os alunos
- Média geral de cada aluno
- Identificação de alunos em risco de reprovação

### Relatório de Frequência Geral

**Endpoint:** `GET /report/attendance/classroom/{classroomId}?startDate=2025-01-01&endDate=2025-03-31`

**Informações:**
- Total de aulas no período
- Percentual de frequência por aluno
- Identificação de alunos com frequência abaixo de 75%

### Relatório de Aprovação

**Endpoint:** `GET /report/approval?schoolYear=2025`

**Estatísticas:**
- Total de alunos
- Aprovados
- Reprovados
- Em recuperação
- Taxa de aprovação geral

### Relatório de Evasão

**Endpoint:** `GET /report/dropout`

**Mostra:**
- Total de alunos inativos/evadidos
- Taxa de evasão
- Análise por período

### Exportar Dados

**CSV:** `GET /report/export/csv/{classroomId}`
**Excel:** `GET /report/export/excel/{classroomId}`

---

## Configurações do Sistema

### Gerenciar Eventos Escolares

**Criar Evento:**
```json
{
  "title": "Reunião de Pais",
  "description": "Reunião do 1º Bimestre",
  "eventDate": "2025-04-15",
  "eventType": "MEETING | HOLIDAY | EXAM | OTHER",
  "location": "Auditório Principal"
}
```

### Períodos Letivos

O sistema trabalha com 4 bimestres (quarters):
- `FIRST`: 1º Bimestre
- `SECOND`: 2º Bimestre
- `THIRD`: 3º Bimestre
- `FOURTH`: 4º Bimestre

### Critérios de Aprovação

**Configuração Padrão:**
- Média para aprovação: 7.0
- Média para recuperação: 5.0 a 6.9
- Reprovação: abaixo de 5.0
- Frequência mínima: 75%

**Status do Boletim:**
- `APPROVED`: Aprovado (média ≥ 7.0)
- `RECOVERY`: Recuperação (média entre 5.0 e 6.9)
- `FAILED`: Reprovado (média < 5.0)
- `STUDYING`: Ainda cursando

---

## Boas Práticas

### Início do Ano Letivo

1. Configurar turmas e salas
2. Cadastrar professores
3. Atribuir disciplinas aos professores
4. Matricular alunos nas turmas
5. Verificar capacidade das turmas

### Durante o Ano Letivo

1. Monitorar frequência semanalmente
2. Gerar relatórios de notas ao final de cada bimestre
3. Identificar alunos em risco de evasão
4. Acompanhar taxa de ocupação das turmas

### Fim do Bimestre

1. Verificar lançamento de todas as notas
2. Gerar boletins
3. Gerar relatórios para reunião de pais
4. Identificar alunos em recuperação

### Fim do Ano Letivo

1. Gerar relatório final de aprovação
2. Processar recuperações finais
3. Gerar históricos escolares
4. Exportar dados para arquivo

---

## Suporte e Documentação

- **Documentação da API:** http://localhost:8080/swagger-ui.html
- **Suporte Técnico:** support@schoolmanagement.com
- **Guia do Professor:** [TEACHER_MANUAL.md](TEACHER_MANUAL.md)
- **Guia do Responsável:** [GUARDIAN_MANUAL.md](GUARDIAN_MANUAL.md)
