package com.dentalcare.quiz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dentalcare.quiz.R
import com.dentalcare.quiz.router.PatientDetails
import com.dentalcare.quiz.router.Records

@Composable
fun Home(
    navController: NavController
) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.size(80.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(180.dp)
                    )
                    Text(text = stringResource(id = R.string.app_name))
                    Spacer(modifier = Modifier.size(40.dp))
                    Button(onClick = {
                        navController.navigate(PatientDetails)
                    }) {
                        Text(text = stringResource(R.string.start_analysis))
                    }
                    Button(onClick = {
                        navController.navigate(Records)
                    }) {
                        Text(text = stringResource(R.string.view_records))
                    }
                }

                Text(
                    text = stringResource(R.string.developed_by_university),
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    )
}