package com.autofisio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.autofisio.engine.RiskEngine
import com.autofisio.model.*

class AssessmentViewModel : ViewModel() {

    var userData by mutableStateOf(UserData())
        private set

    var answers by mutableStateOf(QuestionnaireAnswers())
        private set

    var assessment by mutableStateOf<RiskAssessment?>(null)
        private set

    var currentQuestion by mutableStateOf(0)
        private set

    // --- User Data ---

    fun updateNome(nome: String) {
        userData = userData.copy(nome = nome)
    }

    fun updateIdade(idade: String) {
        userData = userData.copy(idade = idade)
    }

    fun updateEsporte(esporte: String) {
        userData = userData.copy(esporte = esporte)
    }

    fun isUserDataValid(): Boolean {
        return userData.nome.isNotBlank() &&
                userData.idade.isNotBlank() &&
                userData.esporte.isNotBlank()
    }

    // --- Questionario ---

    fun setTeveLesao(value: Boolean) {
        answers = answers.copy(teveLesao = value)
        if (!value) {
            answers = answers.copy(localLesao = null)
        }
    }

    fun setLocalLesao(local: String) {
        answers = answers.copy(localLesao = local)
    }

    fun setNivelDor(nivel: NivelDor) {
        answers = answers.copy(nivelDor = nivel)
    }

    fun setAumentoVolume(aumento: AumentoVolume) {
        answers = answers.copy(aumentoVolume = aumento)
    }

    fun setFrequenciaTreino(freq: FrequenciaTreino) {
        answers = answers.copy(frequenciaTreino = freq)
    }

    fun setObjetivo(obj: Objetivo) {
        answers = answers.copy(objetivo = obj)
    }

    fun nextQuestion() {
        if (currentQuestion < 4) {
            currentQuestion++
        }
    }

    fun previousQuestion() {
        if (currentQuestion > 0) {
            currentQuestion--
        }
    }

    fun isCurrentQuestionAnswered(): Boolean {
        return when (currentQuestion) {
            0 -> answers.teveLesao != null && (answers.teveLesao == false || answers.localLesao != null)
            1 -> answers.nivelDor != null
            2 -> answers.aumentoVolume != null
            3 -> answers.frequenciaTreino != null
            4 -> answers.objetivo != null
            else -> false
        }
    }

    // --- Avaliacao ---

    fun calcularAvaliacao() {
        assessment = RiskEngine.avaliar(userData, answers)
    }

    fun getRelatorioTexto(): String {
        val result = assessment ?: return ""
        return RiskEngine.gerarRelatorioTexto(userData, result)
    }

    // --- Reset ---

    fun resetAll() {
        userData = UserData()
        answers = QuestionnaireAnswers()
        assessment = null
        currentQuestion = 0
    }
}
