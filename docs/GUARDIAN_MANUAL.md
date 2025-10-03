# Manual do Responsável - Sistema de Gestão Escolar

## Sumário
1. [Visão Geral](#visão-geral)
2. [Meu Cadastro](#meu-cadastro)
3. [Dados dos Meus Filhos](#dados-dos-meus-filhos)
4. [Acompanhamento de Notas](#acompanhamento-de-notas)
5. [Acompanhamento de Frequência](#acompanhamento-de-frequência)
6. [Boletins](#boletins)
7. [Eventos Escolares](#eventos-escolares)

---

## Visão Geral

Como responsável legal pelo aluno, você pode acompanhar toda a vida acadêmica de seus filhos através do sistema. Este manual explica como utilizar as funcionalidades disponíveis para você.

### O Que Você Pode Fazer

- Consultar dados cadastrais dos seus filhos
- Acompanhar notas e médias em tempo real
- Verificar frequência e faltas
- Visualizar boletins de todos os bimestres
- Consultar calendário de eventos escolares
- Atualizar seus dados de contato

---

## Meu Cadastro

### Consultar Meus Dados

**Endpoint:** `GET /guardian/{guardianId}`

Exibe suas informações cadastrais:
- Nome completo
- Email de contato
- Telefone
- Endereço
- Profissão e local de trabalho
- Renda familiar

### Atualizar Dados de Contato

**Endpoint:** `PUT /guardian/{guardianId}`

**É importante manter atualizados:**
- Telefone de contato
- Email
- Endereço residencial

```json
{
  "profession": "Engenheiro",
  "workplace": "Empresa XYZ",
  "familyIncome": 5000.00
}
```

**Por que atualizar?**
- A escola pode precisar entrar em contato urgentemente
- Avisos importantes são enviados por email
- Informações para auxílios e bolsas

---

## Dados dos Meus Filhos

### Consultar Meus Filhos

**Endpoint:** `GET /student/guardian/{guardianId}`

Lista todos os alunos vinculados a você como responsável.

### Informações do Aluno

**Endpoint:** `GET /student/enrollment/{enrollmentNumber}`

Consulte dados do aluno:
- Nome completo
- Número de matrícula
- Turma atual
- Status (Ativo, Transferido)
- Observações da escola

### Número de Matrícula

Cada aluno possui um número de matrícula único no formato:
- **YYYY######**
- Exemplo: 2025000001

**Guarde este número!** Ele é necessário para várias consultas.

---

## Acompanhamento de Notas

### Ver Todas as Notas do Meu Filho

**Endpoint:** `GET /grade/student/{studentId}`

**O que aparece:**
- Todas as disciplinas
- Todas as avaliações (provas, trabalhos, testes)
- Notas de cada avaliação
- Data de lançamento
- Nome do professor

### Ver Média em Uma Disciplina

**Endpoint:** `GET /grade/average?studentId={id}&subjectId={id}&quarter={quarter}`

**Bimestres disponíveis:**
- `FIRST` - 1º Bimestre
- `SECOND` - 2º Bimestre
- `THIRD` - 3º Bimestre
- `FOURTH` - 4º Bimestre

**Como Funciona a Média:**
- O sistema calcula automaticamente a média de todas as avaliações do bimestre
- Exemplo: Se seu filho tirou 7.0, 8.0 e 9.0, a média será 8.0

### Entender as Notas

**Escala de 0 a 10:**
- 9.0 a 10.0 - Excelente
- 7.0 a 8.9 - Bom
- 5.0 a 6.9 - Regular (pode precisar de recuperação)
- 0.0 a 4.9 - Insuficiente (necessita recuperação)

---

## Acompanhamento de Frequência

### Consultar Frequência

**Endpoint:** `GET /attendance/student/{studentId}?startDate=2025-01-01&endDate=2025-03-31`

**Informações exibidas:**
- Total de aulas no período
- Número de presenças
- Número de faltas
- Faltas justificadas
- Faltas não justificadas
- Percentual de frequência

### Calcular Percentual de Frequência

**Endpoint:** `GET /attendance/calculate/{studentId}?startDate=2025-01-01&endDate=2025-12-31`

**Atenção:**
- É necessário no mínimo **75% de frequência** para aprovação
- Abaixo disso, o aluno pode reprovar mesmo com notas boas

### Justificar Falta

Se seu filho faltou por motivo de saúde ou outro motivo justificável:

1. Procure a secretaria da escola
2. Apresente o atestado ou documentação
3. A escola registrará a justificativa no sistema

**Documentos aceitos:**
- Atestado médico
- Declaração de comparecimento em consulta
- Documentos de participação em eventos oficiais
- Outros documentos conforme regimento escolar

---

## Boletins

### Ver Boletim de Um Bimestre

**Endpoint:** `GET /grade/report-card/{studentId}/{quarter}?schoolYear=2025`

**Exemplo para ver o 1º bimestre:**
```
GET /grade/report-card/1/FIRST?schoolYear=2025
```

### Conteúdo do Boletim

O boletim mostra:

**Para cada disciplina:**
- Nome da disciplina
- Média do bimestre
- Professor responsável

**Informações gerais:**
- Média geral (todas as disciplinas)
- Status: APROVADO, RECUPERAÇÃO ou REPROVADO
- Frequência do período
- Observações dos professores

### Entender o Status

**APROVADO**
- Média ≥ 7.0 em todas as disciplinas
- Frequência ≥ 75%
- Parabéns! Seu filho está indo bem!

**RECUPERAÇÃO**
- Média entre 5.0 e 6.9 em uma ou mais disciplinas
- Seu filho precisará fazer recuperação
- Converse com os professores sobre o plano de recuperação

**REPROVADO**
- Média < 5.0 em uma ou mais disciplinas, ou
- Frequência < 75%
- É importante buscar apoio pedagógico

**STUDYING** (Estudando)
- Ano letivo ainda em andamento
- Status será atualizado ao final do ano

### Consultar Histórico Completo

**Endpoint:** `GET /student/academic-history/{enrollmentNumber}`

Mostra todo o histórico acadêmico:
- Todos os bimestres
- Todas as notas
- Toda a frequência
- Todos os boletins

---

## Eventos Escolares

### Ver Calendário de Eventos

**Endpoint:** `GET /event/all`

Tipos de eventos:
- `MEETING` - Reuniões de pais
- `HOLIDAY` - Feriados e recesso
- `EXAM` - Provas e avaliações
- `OTHER` - Outros eventos

### Detalhes de Um Evento

**Endpoint:** `GET /event/{eventId}`

Informações:
- Título do evento
- Data e horário
- Local
- Descrição detalhada

**Importante:** Fique atento às reuniões de pais! É o momento de conversar pessoalmente com os professores.

---

## Como Ajudar Seu Filho

### Se as Notas Estão Boas

- Continue incentivando os estudos
- Parabenize pelo esforço
- Mantenha uma rotina de estudos
- Acompanhe as tarefas de casa

### Se as Notas Estão Baixas

1. **Converse com seu filho**
   - Entenda as dificuldades
   - Pergunte se há problemas em sala

2. **Verifique a frequência**
   - Faltas podem prejudicar o aprendizado
   - Certifique-se que está indo às aulas

3. **Entre em contato com a escola**
   - Fale com os professores
   - Pergunte sobre reforço escolar
   - Participe das reuniões de pais

4. **Crie uma rotina de estudos**
   - Horário fixo para estudar
   - Local tranquilo
   - Ajude com as tarefas

### Se a Frequência Está Baixa

**Atenção:** Frequência inferior a 75% pode reprovar!

- Verifique o motivo das faltas
- Consulte um médico se há problemas de saúde
- Converse com a escola sobre possíveis soluções
- Justifique as faltas sempre que possível

---

## Calendário Escolar

### Bimestres

**1º Bimestre:** Fevereiro a Abril
**2º Bimestre:** Maio a Junho
**3º Bimestre:** Agosto a Setembro
**4º Bimestre:** Outubro a Dezembro

### Períodos Importantes

- **Final de cada bimestre:** Divulgação de boletins
- **Após cada bimestre:** Reuniões de pais
- **Julho:** Recesso escolar
- **Dezembro:** Recuperação final

---

## Perguntas Frequentes

**1. Com que frequência devo verificar as notas?**
Recomendamos verificar semanalmente, principalmente após provas e trabalhos.

**2. Recebi uma notificação de falta. O que fazer?**
Verifique a data da falta. Se foi justificada, procure a secretaria com o documento. Se não foi justificada, converse com seu filho.

**3. As notas podem mudar depois de lançadas?**
Sim, professores podem corrigir erros de lançamento. O boletim sempre mostra os dados mais atualizados.

**4. Como sei se meu filho precisa de recuperação?**
Se a média em qualquer disciplina for inferior a 7.0, ele precisará de recuperação. O status do boletim indicará "RECUPERAÇÃO".

**5. Posso acompanhar vários filhos no sistema?**
Sim! Basta usar o endpoint que lista todos os alunos vinculados a você como responsável.

**6. Quando os boletins ficam disponíveis?**
Geralmente 1-2 semanas após o fim do bimestre, quando todos os professores terminam de lançar as notas.

---

## Direitos e Deveres

### Seus Direitos

- Acesso a todas as informações acadêmicas do seu filho
- Participar de reuniões e eventos escolares
- Ser informado sobre problemas de rendimento ou frequência
- Solicitar reunião com professores e coordenação

### Seus Deveres

- Manter dados cadastrais atualizados
- Acompanhar o desempenho acadêmico regularmente
- Participar das reuniões de pais
- Justificar faltas quando necessário
- Incentivar e apoiar os estudos em casa

---

## Suporte

**Dúvidas sobre o sistema?**
- Email: support@schoolmanagement.com

**Questões pedagógicas?**
- Entre em contato diretamente com a escola
- Solicite reunião com professores ou coordenação

**Documentação Técnica da API:**
- http://localhost:8080/swagger-ui.html
