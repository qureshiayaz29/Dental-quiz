package com.dentalcare.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            FullMouthRehabilitationTheme {
                NavHost(navController = navController, startDestination = Home) {
                    composable<Home> {
                        Home(navController)
                    }

                    composable<PatientDetails> {
                        PatientDetails(navController)
                    }

                    composable<Quiz> {
                        val args = it.toRoute<Quiz>()
                        with(args) {
                            Quiz(
                                navController = navController,
                                name = name,
                                number = number,
                                dob = dob
                            )
                        }
                    }

                    composable<Records> {
                        Records(navController)
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