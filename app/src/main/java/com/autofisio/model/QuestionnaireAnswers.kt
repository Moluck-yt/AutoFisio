package com.autofisio.model

data class QuestionnaireAnswers(
    // P1: Lesao nos ultimos 12 meses
    val teveLesao: Boolean? = null,
    val localLesao: String? = null,

    // P2: Dor durante/apos treino
    val nivelDor: NivelDor? = null,

    // P3: Volume de treino aumentou
    val aumentoVolume: AumentoVolume? = null,

    // P4: Frequencia semanal
    val frequenciaTreino: FrequenciaTreino? = null,

    // P5: Objetivo principal
    val objetivo: Objetivo? = null
)

enum class NivelDor(val label: String) {
    NENHUMA("Não sinto dor"),
    LEVE("Sinto dor leve"),
    MODERADA("Sinto dor moderada"),
    FORTE("Sinto dor forte")
}

enum class AumentoVolume(val label: String) {
    NAO("Não"),
    UM_POUCO("Aumentou um pouco"),
    MUITO("Aumentou muito"),
    NAO_SEI("Não sei")
}

enum class FrequenciaTreino(val label: String) {
    UM_A_DOIS("1 a 2 dias"),
    TRES_A_QUATRO("3 a 4 dias"),
    CINCO_A_SEIS("5 a 6 dias"),
    TODOS_OS_DIAS("Todos os dias")
}

enum class Objetivo(val label: String) {
    EVITAR_LESOES("Evitar lesões"),
    MELHORAR_PERFORMANCE("Melhorar performance"),
    VOLTAR_DE_LESAO("Voltar de uma lesão"),
    COMECAR_COM_SEGURANCA("Começar a treinar com segurança")
}
