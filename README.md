# AutoFisio

Aplicativo Android de auto-avaliação fisioterapêutica esportiva. O app coleta dados do usuário através de um questionário baseado em evidências científicas, calcula um score de risco de lesão e gera um relatório personalizado com recomendações.

## Problema

Atletas amadores e praticantes de esportes não têm acesso rápido a uma avaliação de risco de lesões. Muitos se lesionam por ignorar sinais de sobrecarga, dor ou histórico de lesões anteriores.

## Público-alvo

- Atletas amadores e praticantes de esportes
- Personal trainers que querem triagem rápida de alunos
- Estudantes de fisioterapia e educação física

## Funcionalidades

- Cadastro de dados básicos (nome, idade, esporte)
- Questionário de 5 perguntas com subperguntas condicionais
- Motor de avaliação rule-based que calcula score de risco (0-13 pontos)
- Classificação em 3 níveis: Baixo, Moderado e Alto risco
- Relatório com recomendações personalizadas
- Compartilhamento do relatório via Email e WhatsApp

## Questionário

1. **Lesão recente** — Histórico de lesão nos últimos 12 meses (com local da lesão)
2. **Dor atual** — Nível de dor durante ou após o treino
3. **Volume de treino** — Aumento recente na carga de treino (Acute:Chronic Workload Ratio)
4. **Frequência** — Dias de treino por semana
5. **Objetivo** — Meta principal do usuário com o esporte

## Fluxo do Usuário

1. Tela de boas-vindas → Iniciar avaliação
2. Preenche dados pessoais
3. Responde as 5 perguntas
4. Visualiza relatório com nível de risco e recomendações
5. Compartilha por email ou WhatsApp

## Stack

- **Kotlin** + **Jetpack Compose**
- **MVVM** (ViewModel)
- **Navigation Compose**
- **Material 3**

## Estrutura do Projeto

```
app/src/main/java/com/autofisio/
├── MainActivity.kt
├── navigation/
│   └── NavGraph.kt
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── screens/
│       ├── WelcomeScreen.kt
│       ├── UserDataScreen.kt
│       ├── QuestionnaireScreen.kt
│       └── ReportScreen.kt
├── model/
│   ├── UserData.kt
│   ├── QuestionnaireAnswers.kt
│   └── RiskAssessment.kt
├── viewmodel/
│   └── AssessmentViewModel.kt
└── engine/
    └── RiskEngine.kt
```

## Como Rodar

1. Clone o repositório
2. Abra no Android Studio
3. Sincronize o Gradle
4. Rode no emulador ou dispositivo (minSdk 26)
