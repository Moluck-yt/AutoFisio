package com.autofisio.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.autofisio.ui.screens.*
import com.autofisio.viewmodel.AssessmentViewModel

object Routes {
    const val WELCOME = "welcome"
    const val USER_DATA = "user_data"
    const val QUESTIONNAIRE = "questionnaire"
    const val REPORT = "report"
}

@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: AssessmentViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onStartClick = {
                    navController.navigate(Routes.USER_DATA)
                }
            )
        }

        composable(Routes.USER_DATA) {
            UserDataScreen(
                viewModel = viewModel,
                onNext = {
                    navController.navigate(Routes.QUESTIONNAIRE)
                }
            )
        }

        composable(Routes.QUESTIONNAIRE) {
            QuestionnaireScreen(
                viewModel = viewModel,
                onFinish = {
                    navController.navigate(Routes.REPORT)
                }
            )
        }

        composable(Routes.REPORT) {
            ReportScreen(
                viewModel = viewModel,
                onNewAssessment = {
                    viewModel.resetAll()
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
