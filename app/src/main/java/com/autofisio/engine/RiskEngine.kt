package com.autofisio.engine

import com.autofisio.model.*

object RiskEngine {

    fun avaliar(userData: UserData, answers: QuestionnaireAnswers): RiskAssessment {
        val score = calcularScore(answers)
        val nivel = classificarRisco(score)
        val recomendacoes = gerarRecomendacoes(userData, answers, nivel)
        return RiskAssessment(score = score, nivel = nivel, recomendacoes = recomendacoes)
    }

    private fun calcularScore(answers: QuestionnaireAnswers): Int {
        var score = 0

        // P1: Lesao recente (0 ou 3 pts + bonus por local)
        if (answers.teveLesao == true) {
            score += 3
            if (answers.localLesao == "Joelho" || answers.localLesao == "Tornozelo") {
                score += 1
            }
        }

        // P2: Dor atual (0 a 3 pts)
        score += when (answers.nivelDor) {
            NivelDor.NENHUMA -> 0
            NivelDor.LEVE -> 1
            NivelDor.MODERADA -> 2
            NivelDor.FORTE -> 3
            null -> 0
        }

        // P3: Aumento de volume (0 a 3 pts)
        score += when (answers.aumentoVolume) {
            AumentoVolume.NAO -> 0
            AumentoVolume.UM_POUCO -> 1
            AumentoVolume.MUITO -> 3
            AumentoVolume.NAO_SEI -> 2
            null -> 0
        }

        // P4: Frequencia (0 a 3 pts)
        score += when (answers.frequenciaTreino) {
            FrequenciaTreino.UM_A_DOIS -> 0
            FrequenciaTreino.TRES_A_QUATRO -> 1
            FrequenciaTreino.CINCO_A_SEIS -> 2
            FrequenciaTreino.TODOS_OS_DIAS -> 3
            null -> 0
        }

        return score
    }

    private fun classificarRisco(score: Int): NivelRisco {
        return when {
            score <= 3 -> NivelRisco.BAIXO
            score <= 7 -> NivelRisco.MODERADO
            else -> NivelRisco.ALTO
        }
    }

    private fun gerarRecomendacoes(
        userData: UserData,
        answers: QuestionnaireAnswers,
        nivel: NivelRisco
    ): List<String> {
        val recs = mutableListOf<String>()

        // Recomendacoes por nivel de risco
        when (nivel) {
            NivelRisco.BAIXO -> {
                recs.add("Seu risco de lesão está baixo. Continue mantendo bons hábitos de treino.")
                recs.add("Mantenha um aquecimento adequado antes dos treinos.")
                recs.add("Inclua exercícios de mobilidade e alongamento na sua rotina.")
            }
            NivelRisco.MODERADO -> {
                recs.add("Atenção: seu risco de lesão está moderado.")
                recs.add("Considere reduzir a intensidade dos treinos por algumas semanas.")
                recs.add("Priorize exercícios de fortalecimento e estabilização articular.")
                recs.add("Monitore sinais de fadiga e dor durante os treinos.")
            }
            NivelRisco.ALTO -> {
                recs.add("Alerta: seu risco de lesão está alto!")
                recs.add("Recomendamos fortemente uma avaliação presencial com um fisioterapeuta esportivo.")
                recs.add("Reduza o volume e intensidade dos treinos imediatamente.")
                recs.add("Não ignore sinais de dor — eles indicam sobrecarga no seu corpo.")
            }
        }

        // Recomendacoes por lesao recente
        if (answers.teveLesao == true) {
            val local = answers.localLesao ?: "região afetada"
            recs.add("Você reportou lesão recente em $local. Reforce o trabalho de reabilitação e prevenção nessa região.")
            if (answers.localLesao == "Joelho") {
                recs.add("Para o joelho: fortaleça quadríceps, glúteos e trabalhe propriocepção.")
            }
            if (answers.localLesao == "Tornozelo") {
                recs.add("Para o tornozelo: exercícios de equilíbrio e fortalecimento dos estabilizadores são essenciais.")
            }
        }

        // Recomendacoes por dor
        if (answers.nivelDor == NivelDor.FORTE) {
            recs.add("Dor forte durante ou após o treino é um sinal de alerta. Procure um profissional antes de continuar treinando.")
        } else if (answers.nivelDor == NivelDor.MODERADA) {
            recs.add("A dor moderada que você sente pode indicar início de sobrecarga. Fique atento à evolução.")
        }

        // Recomendacoes por volume
        if (answers.aumentoVolume == AumentoVolume.MUITO) {
            recs.add("O aumento abrupto no volume de treino é um dos principais fatores de lesão. Aplique a regra dos 10%: não aumente mais que 10% por semana.")
        }

        // Recomendacoes por frequencia
        if (answers.frequenciaTreino == FrequenciaTreino.TODOS_OS_DIAS) {
            recs.add("Treinar todos os dias sem descanso adequado aumenta o risco de overtraining. Inclua pelo menos 1-2 dias de recuperação por semana.")
        }

        // Recomendacoes por objetivo
        when (answers.objetivo) {
            Objetivo.EVITAR_LESOES -> {
                recs.add("Para prevenção de lesões: invista em aquecimento, fortalecimento muscular e respeite os períodos de descanso.")
            }
            Objetivo.MELHORAR_PERFORMANCE -> {
                recs.add("Para melhorar performance com segurança: periodize seu treino e monitore a relação carga aguda/crônica.")
            }
            Objetivo.VOLTAR_DE_LESAO -> {
                recs.add("Retorno de lesão exige acompanhamento profissional. Siga um protocolo progressivo de retorno ao esporte.")
            }
            Objetivo.COMECAR_COM_SEGURANCA -> {
                recs.add("Para começar com segurança: inicie com cargas leves, aprenda a técnica correta e aumente gradualmente.")
            }
            null -> {}
        }

        // Sempre inclui
        recs.add("⚕️ Consulte um fisioterapeuta esportivo para uma avaliação presencial completa.")

        return recs
    }

    fun gerarRelatorioTexto(userData: UserData, assessment: RiskAssessment): String {
        val sb = StringBuilder()
        sb.appendLine("═══════════════════════════════")
        sb.appendLine("  AUTOFISIO - RELATÓRIO DE AVALIAÇÃO")
        sb.appendLine("  Avaliação Fisioterapêutica Esportiva")
        sb.appendLine("═══════════════════════════════")
        sb.appendLine()
        sb.appendLine("Paciente: ${userData.nome}")
        sb.appendLine("Idade: ${userData.idade} anos")
        sb.appendLine("Esporte: ${userData.esporte}")
        sb.appendLine()
        sb.appendLine("───────────────────────────────")
        sb.appendLine("RESULTADO: ${assessment.nivel.label.uppercase()}")
        sb.appendLine("Score: ${assessment.score}/13")
        sb.appendLine("───────────────────────────────")
        sb.appendLine()
        sb.appendLine("RECOMENDAÇÕES:")
        sb.appendLine()
        assessment.recomendacoes.forEachIndexed { index, rec ->
            sb.appendLine("${index + 1}. $rec")
            sb.appendLine()
        }
        sb.appendLine("───────────────────────────────")
        sb.appendLine("Gerado pelo AutoFisio")
        sb.appendLine("Este relatório não substitui uma avaliação presencial.")
        return sb.toString()
    }
}
