# AutoFisio

Aplicativo Android de auto-avaliação fisioterapêutica na área esportiva, desenvolvido em **Kotlin** com **Jetpack Compose**.

---

## 1. Problema

Atletas amadores e praticantes de esportes não têm acesso rápido a uma avaliação de risco de lesões. Muitos se lesionam por ignorar sinais de sobrecarga, dor ou histórico de lesões anteriores.

## 2. Público-alvo

- Atletas amadores e praticantes de esportes
- Personal trainers que querem triagem rápida de alunos
- Estudantes de fisioterapia e educação física

## 3. Proposta de Valor

Um app simples e offline que realiza uma auto-avaliação esportiva, calcula um score de risco de lesão e gera um relatório personalizado com recomendações — sem necessidade de internet ou servidores externos.

## 4. Funcionalidades Essenciais (MVP)

| Funcionalidade | Descrição |
|---|---|
| Tela de boas-vindas | Apresentação do app com botão para iniciar |
| Cadastro de dados | Coleta nome, idade e esporte praticado |
| Questionário | 5 perguntas com subperguntas condicionais |
| Motor de avaliação | Algoritmo rule-based que calcula score de risco (0-13 pts) |
| Relatório | Exibe nível de risco (Baixo/Moderado/Alto) com recomendações |
| Compartilhamento | Envio do relatório por Email ou WhatsApp |

## 5. Fluxo do Usuário

```
[Boas-vindas] → [Dados Pessoais] → [Questionário 1/5 ... 5/5] → [Relatório] → [Compartilhar]
```

1. Usuário abre o app e toca em "Iniciar Avaliação"
2. Preenche nome, idade e esporte praticado
3. Responde as 5 perguntas do questionário (com barra de progresso)
4. O app calcula o score de risco internamente
5. Visualiza o relatório com nível de risco e recomendações personalizadas
6. Compartilha o relatório por email ou WhatsApp

---

## Arquitetura e Decisões Técnicas

### Stack Tecnológica

| Tecnologia | Função |
|---|---|
| **Kotlin** | Linguagem principal do projeto |
| **Jetpack Compose** | Framework de UI declarativa do Android |
| **Material 3** | Sistema de design (cores, tipografia, componentes) |
| **Navigation Compose** | Navegação entre telas sem Activities extras |
| **ViewModel (MVVM)** | Gerenciamento de estado reativo |
| **Gradle Kotlin DSL** | Sistema de build |

### Padrão Arquitetural — MVVM

O app segue o padrão **Model-View-ViewModel** recomendado pelo Android:

```
┌──────────────────────────────────────────────┐
│                    VIEW                       │
│  (Compose Screens: Welcome, UserData,         │
│   Questionnaire, Report)                      │
│         ↕ observa estado                      │
│               ViewModel                       │
│  (AssessmentViewModel: gerencia estado        │
│   do fluxo e coordena a avaliação)            │
│         ↕ usa                                 │
│                MODEL                          │
│  (UserData, QuestionnaireAnswers,             │
│   RiskAssessment, RiskEngine)                 │
└──────────────────────────────────────────────┘
```

- **View (Screens):** Telas em Jetpack Compose que exibem a UI e reagem a mudanças de estado
- **ViewModel:** Mantém o estado da avaliação (dados do usuário, respostas, resultado) e sobrevive a rotações de tela
- **Model:** Data classes que representam os dados e o `RiskEngine` que contém a lógica de negócio

### Estrutura de Pastas

```
app/src/main/java/com/autofisio/
├── MainActivity.kt                 # Entry point — configura tema e navegação
├── navigation/
│   └── NavGraph.kt                 # Define rotas e transições entre telas
├── ui/
│   ├── theme/
│   │   ├── Color.kt                # Paleta de cores (azul médico + cores de risco)
│   │   ├── Theme.kt                # Tema Material 3 customizado
│   │   └── Type.kt                 # Tipografia personalizada
│   └── screens/
│       ├── WelcomeScreen.kt        # Tela inicial com gradiente e logo
│       ├── UserDataScreen.kt       # Formulário de dados pessoais
│       ├── QuestionnaireScreen.kt  # Questionário com 5 perguntas
│       └── ReportScreen.kt         # Relatório final com compartilhamento
├── model/
│   ├── UserData.kt                 # Data class: nome, idade, esporte
│   ├── QuestionnaireAnswers.kt     # Data class + enums das respostas
│   └── RiskAssessment.kt           # Data class: score, nível, recomendações
├── viewmodel/
│   └── AssessmentViewModel.kt      # ViewModel central do fluxo
└── engine/
    └── RiskEngine.kt               # Motor de avaliação rule-based
```

### Motor de Avaliação (RiskEngine)

O `RiskEngine` é o núcleo do app. Ele recebe as respostas do questionário e calcula o risco usando um sistema de pontuação:

**Sistema de Pontuação:**

| Pergunta | Resposta | Pontos |
|---|---|---|
| Lesão nos últimos 12 meses | Não | 0 |
| | Sim | 3 (+1 se joelho/tornozelo) |
| Dor no treino | Nenhuma | 0 |
| | Leve | 1 |
| | Moderada | 2 |
| | Forte | 3 |
| Aumento de volume | Não | 0 |
| | Um pouco | 1 |
| | Não sei | 2 |
| | Muito | 3 |
| Frequência semanal | 1-2 dias | 0 |
| | 3-4 dias | 1 |
| | 5-6 dias | 2 |
| | Todos os dias | 3 |
| Objetivo | (usado para personalizar recomendações) | — |

**Classificação de Risco:**

| Score | Nível | Cor |
|---|---|---|
| 0–3 | Baixo | Verde |
| 4–7 | Moderado | Amarelo |
| 8–13 | Alto | Vermelho |

Após calcular o score, o engine gera recomendações personalizadas baseadas em:
- Nível de risco geral
- Local da lesão (se houver)
- Nível de dor reportado
- Aumento de volume de treino
- Objetivo do usuário

### Navegação

O app usa **Navigation Compose** com 4 rotas:

```
WELCOME → USER_DATA → QUESTIONNAIRE → REPORT
                                         ↓
                                   (Nova Avaliação → volta ao WELCOME)
```

Toda navegação é gerenciada pelo `NavHostController` sem necessidade de múltiplas Activities.

### Compartilhamento

O relatório é compartilhado usando `Intent.ACTION_SEND` nativo do Android:

- **Email:** Abre o cliente de email com assunto e corpo pré-preenchidos
- **WhatsApp:** Envia diretamente para o WhatsApp (com fallback para compartilhamento genérico caso o WhatsApp não esteja instalado)

### UI e Design

- **Paleta de cores:** Tons de azul (médico/profissional) com cores semafóricas para risco (verde/amarelo/vermelho)
- **Componentes:** Cards com radio buttons para seleções, barra de progresso no questionário, indicador circular de score no relatório
- **Adaptive Icon:** Ícone do app com cruz médica branca sobre fundo azul

---

## Requisitos

- Android Studio Hedgehog ou superior
- Android SDK 34
- Dispositivo/emulador com Android 8.0+ (API 26)

## Como Rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/Moluck-yt/AutoFisio.git
   ```
2. Abra o projeto no Android Studio
3. Aguarde a sincronização do Gradle
4. Rode no emulador ou dispositivo conectado
