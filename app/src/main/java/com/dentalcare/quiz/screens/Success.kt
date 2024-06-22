package com.dentalcare.quiz.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dentalcare.quiz.R
import com.dentalcare.quiz.router.Home
import com.dentalcare.quiz.router.Records
import com.dentalcare.quiz.router.Success
import com.dentalcare.quiz.util.getResultFromScore

@Composable
fun Success(
    navController: NavController,
    result: Int
) {
    Scaffold(
        topBar = {},
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight()
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                ) {
                    SuccessLottieView()
                }
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    text = stringResource(R.string.record_saved_successfully),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    text = stringResource(R.string.score, result),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.result, getResultFromScore(result)),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.padding(2.dp))

                Spacer(modifier = Modifier.padding(12.dp))
                Row(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Button(onClick = {
                        navController.navigate(Home)
                    }) {
                        Text(text = stringResource(R.string.home))
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(onClick = {
                        navController.popBackStack(Success(result), true)
                        navController.navigate(Records)
                    }) {
                        Text(text = stringResource(R.string.view_all_records))
                    }
                }
            }
        }
    )
}

@Composable
fun SuccessLottieView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}