package com.dentalcare.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dentalcare.quiz.router.Home
import com.dentalcare.quiz.router.PatientDetails
import com.dentalcare.quiz.router.Quiz
import com.dentalcare.quiz.router.Records
import com.dentalcare.quiz.router.Success
import com.dentalcare.quiz.screens.Home
import com.dentalcare.quiz.screens.PatientDetails
import com.dentalcare.quiz.screens.Quiz
import com.dentalcare.quiz.screens.Records
import com.dentalcare.quiz.screens.Success
import com.dentalcare.quiz.screens.ui.theme.FullMouthRehabilitationTheme
import com.dentalcare.quiz.viewmodel.AppViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel = AppViewModel()
            FullMouthRehabilitationTheme {
                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> {
                        Home(navController)
                    }

                    composable<PatientDetails> {
                        PatientDetails(navController, viewModel)
                    }

                    composable<Quiz> {
                        Quiz(navController, viewModel)
                    }

                    composable<Records> {
                        Records(navController, viewModel)
                    }

                    composable<Success> {
                        val result = it.toRoute<Success>().result
                        Success(navController, result)
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.getViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}
