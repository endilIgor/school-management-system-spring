# Manual do Professor - Sistema de Gestão Escolar

## Sumário
1. [Visão Geral](#visão-geral)
2. [Minhas Turmas e Disciplinas](#minhas-turmas-e-disciplinas)
3. [Registro de Frequência](#registro-de-frequência)
4. [Lançamento de Notas](#lançamento-de-notas)
5. [Consulta de Alunos](#consulta-de-alunos)
6. [Geração de Boletins](#geração-de-boletins)
7. [Relatórios](#relatórios)

---

## Visão Geral

Como professor, você é responsável por acompanhar o desempenho acadêmico e a frequência dos alunos em suas turmas. Este manual orienta sobre todas as funcionalidades disponíveis para você.

### Suas Responsabilidades

- Registrar presença/falta dos alunos
- Lançar notas de avaliações
- Acompanhar desempenho dos alunos
- Gerar boletins ao final de cada bimestre
- Comunicar situação acadêmica aos responsáveis

---

## Minhas Turmas e Disciplinas

### Consultar Suas Turmas

**Endpoint:** `GET /teacher/{teacherId}/classrooms`

Exibe todas as turmas onde você leciona, incluindo:
- Nome da turma
- Disciplina
- Número de alunos
- Horários

### Listar Alunos de uma Turma

**Endpoint:** `GET /student/classroom/{classroomId}`

Mostra lista completa de alunos matriculados na turma com:
- Nome
- Número de matrícula
- Status (Ativo, Transferido, Evadido)

---

## Registro de Frequência

### Registrar Presença/Falta

**Endpoint:** `POST /attendance`

**Como Registrar:**

```json
{
  "studentId": 1,
  "classroomId": 1,
  "subjectId": 1,
  "teacherId": 1,
  "date": "2025-10-03",
  "present": true,
  "justification": null
}
```

**Campos:**
- `present`: `true` para presença, `false` para falta
- `justification`: Preencher apenas em caso de falta justificada
- `date`: Se não informado, usa a data atual

### Marcar Falta Justificada

Se um aluno apresentar atestado ou justificativa válida posteriormente:

**Endpoint:** `PUT /attendance/{attendanceId}/justify`

```json
{
  "justification": "Atestado médico apresentado em 05/10/2025"
}
```

### Consultar Frequência de Um Aluno

**Endpoint:** `GET /attendance/student/{studentId}?startDate=2025-01-01&endDate=2025-03-31`

Retorna:
- Todas as presenças/faltas no período
- Total de aulas
- Percentual de frequência
- Faltas justificadas e não justificadas

### Consultar Frequência da Turma em Uma Data

**Endpoint:** `GET /attendance/classroom/{classroomId}/date/{date}`

Útil para conferir chamadas anteriores.

---

## Lançamento de Notas

### Registrar Nota de Avaliação

**Endpoint:** `POST /grade`

**Exemplo:**

```json
{
  "studentId": 1,
  "subjectId": 1,
  "classroomId": 1,
  "teacherId": 1,
  "value": 8.5,
  "quarter": "FIRST",
  "evaluationType": "EXAM",
  "recordDate": "2025-03-15"
}
```

**Validações Importantes:**
- Nota deve estar entre **0.0 e 10.0**
- Campo `quarter`:
  - `FIRST` - 1º Bimestre
  - `SECOND` - 2º Bimestre
  - `THIRD` - 3º Bimestre
  - `FOURTH` - 4º Bimestre
- `evaluationType`:
  - `EXAM` - Prova
  - `ASSIGNMENT` - Trabalho
  - `TEST` - Teste
  - `PARTICIPATION` - Participação
  - `PROJECT` - Projeto
  - `OTHER` - Outros

### Atualizar Nota Lançada

**Endpoint:** `PUT /grade/{gradeId}`

```json
{
  "value": 9.0
}
```

**Importante:** Corrija erros de lançamento o quanto antes. Notas afetam diretamente a média do aluno.

### Consultar Notas de Um Aluno

**Endpoint:** `GET /grade/student/{studentId}`

Retorna todas as notas do aluno em todas as disciplinas.

### Consultar Notas da Turma

**Endpoint:** `GET /grade/classroom/{classroomId}/subject/{subjectId}`

Visualize todas as notas lançadas para sua turma em uma disciplina específica.

### Calcular Média de Um Aluno

**Endpoint:** `GET /grade/average?studentId={id}&subjectId={id}&quarter={quarter}`

**Cálculo Automático:**
- O sistema calcula automaticamente a média aritmética de todas as avaliações do bimestre
- Exemplo: Notas 7.0, 8.0 e 9.0 = Média 8.0

---

## Consulta de Alunos

### Buscar Aluno por Matrícula

**Endpoint:** `GET /student/enrollment/{enrollmentNumber}`

Retorna dados completos do aluno.

### Consultar Histórico Acadêmico

**Endpoint:** `GET /student/academic-history/{enrollmentNumber}`

**Informações Disponíveis:**
- Dados pessoais
- Todas as notas (todos os bimestres)
- Registro completo de frequência
- Boletins anteriores
- Observações cadastrais

**Uso Recomendado:**
- Reuniões com responsáveis
- Análise de desempenho
- Identificação de dificuldades

---

## Geração de Boletins

### Gerar Boletim Individual

**Endpoint:** `GET /grade/report-card/{studentId}/{quarter}?schoolYear=2025`

**Quando Gerar:**
- Ao final de cada bimestre
- Após lançamento de todas as notas
- Antes das reuniões de pais

**Conteúdo do Boletim:**
- Todas as disciplinas cursadas
- Média em cada disciplina
- Média geral
- Status: APROVADO, RECUPERAÇÃO ou REPROVADO
- Critérios:
  - Média ≥ 7.0: APROVADO
  - Média entre 5.0 e 6.9: RECUPERAÇÃO
  - Média < 5.0: REPROVADO

### Relatório de Notas da Turma

**Endpoint:** `GET /report/grades/classroom/{classroomId}`

Gera relatório consolidado com:
- Lista de todos os alunos
- Média geral de cada um
- Identificação de alunos em situação de risco

---

## Relatórios

### Relatório de Frequência da Turma

**Endpoint:** `GET /report/attendance/classroom/{classroomId}?startDate=2025-01-01&endDate=2025-03-31`

**Informações:**
- Lista completa de alunos
- Total de aulas no período
- Presenças e faltas de cada aluno
- Percentual de frequência
- Identificação de alunos abaixo de 75% de frequência

**Alerta:** Alunos com frequência inferior a 75% estão em risco de reprovação por falta.

### Calcular Frequência Geral de Um Aluno

**Endpoint:** `GET /attendance/calculate/{studentId}?startDate=2025-01-01&endDate=2025-12-31`

Retorna percentual exato de frequência no período.

### Relatório de Rendimento

**Endpoint:** `GET /grade/classroom/{classroomId}/subject/{subjectId}`

Mostra desempenho geral da turma em sua disciplina:
- Média da turma
- Distribuição de notas
- Alunos com melhor desempenho
- Alunos que precisam de atenção

---

## Fluxo de Trabalho Recomendado

### Diariamente

1. Registrar frequência logo após a aula
2. Anotar observações sobre participação dos alunos
3. Verificar se há justificativas de falta pendentes

### Após Cada Avaliação

1. Lançar notas no sistema em até 7 dias
2. Conferir se todas as notas foram lançadas corretamente
3. Calcular médias parciais para acompanhamento

### Ao Final do Bimestre

1. Verificar se todas as notas foram lançadas
2. Conferir médias de todos os alunos
3. Gerar boletins
4. Identificar alunos para recuperação
5. Preparar relatório para reunião de pais

### Para Recuperação

1. Lançar nota de recuperação
2. Sistema recalcula automaticamente a média
3. Atualizar boletim com novo status

---

## Boas Práticas

### Lançamento de Notas

- Lance as notas assim que corrigir as avaliações
- Confira os valores antes de salvar
- Use tipos de avaliação corretos
- Mantenha critérios transparentes com os alunos

### Registro de Frequência

- Faça chamada no início da aula
- Lance no sistema no mesmo dia
- Em caso de dúvida, confirme com a secretaria
- Aceite justificativas apenas com documentação válida

### Comunicação

- Informe responsáveis sobre alunos em situação de risco
- Documente todas as observações importantes
- Use o campo de observações do boletim quando necessário

### Recuperação

- Ofereça oportunidade de recuperação aos alunos com média abaixo de 7.0
- Comunique claramente os critérios de recuperação
- Lance as notas de recuperação separadamente

---

## Perguntas Frequentes

**1. Como corrigir uma nota lançada errada?**
Use o endpoint `PUT /grade/{gradeId}` com o valor correto.

**2. Posso lançar nota de um aluno que faltou à prova?**
Sim, mas considere dar oportunidade de 2ª chamada. Lance a nota apenas após a avaliação.

**3. O que fazer se um aluno apresentar atestado depois?**
Use o endpoint de justificar falta (`PUT /attendance/{id}/justify`).

**4. Como saber se todos os alunos têm notas lançadas?**
Consulte o relatório de notas da turma. Alunos sem nota aparecerão com média 0.0.

**5. Posso alterar uma nota após gerar o boletim?**
Sim, mas isso afetará a média e o status do aluno. Gere um novo boletim após a alteração.

---

## Suporte

- **Documentação da API:** http://localhost:8080/swagger-ui.html
- **Suporte Técnico:** support@schoolmanagement.com
- **Manual do Administrador:** [ADMIN_MANUAL.md](ADMIN_MANUAL.md)
