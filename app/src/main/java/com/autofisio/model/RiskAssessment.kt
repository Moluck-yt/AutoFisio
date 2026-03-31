package com.autofisio.model

data class RiskAssessment(
    val score: Int,
    val nivel: NivelRisco,
    val recomendacoes: List<String>
)

enum class NivelRisco(val label: String) {
    BAIXO("Risco Baixo"),
    MODERADO("Risco Moderado"),
    ALTO("Risco Alto")
}
