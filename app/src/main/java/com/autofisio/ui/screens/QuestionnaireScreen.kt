package com.autofisio.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autofisio.model.*
import com.autofisio.ui.theme.BluePrimary
import com.autofisio.viewmodel.AssessmentViewModel

@Composable
fun QuestionnaireScreen(
    viewModel: AssessmentViewModel,
    onFinish: () -> Unit
) {
    val currentQ = viewModel.currentQuestion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Progresso
        Text(
            text = "Pergunta ${currentQ + 1} de 5",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = (currentQ + 1) / 5f,
            modifier = Modifier.fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Area de pergunta com scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (currentQ) {
                0 -> Question1(viewModel)
                1 -> Question2(viewModel)
                2 -> Question3(viewModel)
                3 -> Question4(viewModel)
                4 -> Question5(viewModel)
            }
        }

        // Botoes de navegacao
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentQ > 0) {
                OutlinedButton(
                    onClick = { viewModel.previousQuestion() },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text("Anterior")
                }
            }

            Button(
                onClick = {
                    if (currentQ < 4) {
                        viewModel.nextQuestion()
                    } else {
                        viewModel.calcularAvaliacao()
                        onFinish()
                    }
                },
                enabled = viewModel.isCurrentQuestionAnswered(),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(if (currentQ < 4) "Próximo" else "Ver Resultado")
            }
        }
    }
}

@Composable
private fun Question1(viewModel: AssessmentViewModel) {
    QuestionTitle("Você teve alguma lesão nos últimos 12 meses?")

    Spacer(modifier = Modifier.height(16.dp))

    OptionButton(
        text = "Sim",
        selected = viewModel.answers.teveLesao == true,
        onClick = { viewModel.setTeveLesao(true) }
    )
    OptionButton(
        text = "Não",
        selected = viewModel.answers.teveLesao == false,
        onClick = { viewModel.setTeveLesao(false) }
    )

    // Subpergunta condicional
    if (viewModel.answers.teveLesao == true) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Onde foi a lesão?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        val locais = listOf("Joelho", "Tornozelo", "Quadril", "Panturrilha", "Ombro", "Coluna", "Outra")
        locais.forEach { local ->
            OptionButton(
                text = local,
                selected = viewModel.answers.localLesao == local,
                onClick = { viewModel.setLocalLesao(local) }
            )
        }
    }
}

@Composable
private fun Question2(viewModel: AssessmentViewModel) {
    QuestionTitle("Você sente dor durante ou após o treino?")

    Spacer(modifier = Modifier.height(16.dp))

    NivelDor.entries.forEach { nivel ->
        OptionButton(
            text = nivel.label,
            selected = viewModel.answers.nivelDor == nivel,
            onClick = { viewModel.setNivelDor(nivel) }
        )
    }
}

@Composable
private fun Question3(viewModel: AssessmentViewModel) {
    QuestionTitle("Seu volume de treino aumentou nas últimas semanas?")

    Spacer(modifier = Modifier.height(16.dp))

    AumentoVolume.entries.forEach { aumento ->
        OptionButton(
            text = aumento.label,
            selected = viewModel.answers.aumentoVolume == aumento,
            onClick = { viewModel.setAumentoVolume(aumento) }
        )
    }
}

@Composable
private fun Question4(viewModel: AssessmentViewModel) {
    QuestionTitle("Quantos dias por semana você treina?")

    Spacer(modifier = Modifier.height(16.dp))

    FrequenciaTreino.entries.forEach { freq ->
        OptionButton(
            text = freq.label,
            selected = viewModel.answers.frequenciaTreino == freq,
            onClick = { viewModel.setFrequenciaTreino(freq) }
        )
    }
}

@Composable
private fun Question5(viewModel: AssessmentViewModel) {
    QuestionTitle("Qual é o seu principal objetivo hoje?")

    Spacer(modifier = Modifier.height(16.dp))

    Objetivo.entries.forEach { obj ->
        OptionButton(
            text = obj.label,
            selected = viewModel.answers.objetivo == obj,
            onClick = { viewModel.setObjetivo(obj) }
        )
    }
}

@Composable
private fun QuestionTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 28.sp
    )
}

@Composable
private fun OptionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) {
        BluePrimary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    val borderColor = if (selected) {
        BluePrimary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = borderColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
