package com.dentalcare.quiz.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dentalcare.quiz.R
import com.dentalcare.quiz.router.Quiz
import com.dentalcare.quiz.router.Success
import com.dentalcare.quiz.screens.ui.RadioGroup
import com.dentalcare.quiz.util.getQuestion
import com.dentalcare.quiz.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Quiz(
    navController: NavController,
    name: String,
    number: String,
    dob: Long,
) {
    val context = LocalContext.current
    val quizResult = remember { mutableStateMapOf<Int, Int>() }
    val numOfQues = 20
    val unit = 1f / numOfQues
    var progress by remember { mutableFloatStateOf(unit) }
    val currentCount = (progress * numOfQues).toInt()
    val question = getQuestion(currentCount)
    val viewModel = viewModel<AppViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.quiz)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp)
            ) {
                val radioOptions = listOf(
                    question.option[0],
                    question.option[1],
                    question.option[2],
                    question.option[3]
                )

                Text(
                    text = "$currentCount/$numOfQues",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (currentCount > 1) {
                            progress -= unit
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "previous question"
                        )
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    LinearProgressIndicator(
                        progress = {
                            progress
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    IconButton(onClick = {
                        if (currentCount < numOfQues) {
                            progress += unit
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "next question"
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "Ques $currentCount:")
                Text(text = question.question)

                Spacer(modifier = Modifier.padding(8.dp))
                RadioGroup(
                    mItems = radioOptions,
                    selectedIndex = quizResult[currentCount] ?: -1,
                    onOptionSelected = { index ->
                        quizResult[currentCount] = index
                    })
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = {
                    if (currentCount < numOfQues) {
                        progress += unit
                    } else {
                        if (quizResult.keys.size != numOfQues) {
                            val missingAnswer = viewModel.getMissingAnswer(quizResult.keys, numOfQues)
                            progress = unit * missingAnswer
                            Toast.makeText(context,
                                context.getString(R.string.answer_all_questions, missingAnswer), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.insertQuizResult(
                                name = name,
                                number = number,
                                dob = dob,
                                quizAnswers = quizResult.toMap()
                            ) { result ->
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.quiz_recorded_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack(
                                    Quiz(
                                        name = name,
                                        number = number,
                                        dob = dob
                                    ), true
                                )
                                navController.navigate(Success(result.first))
                            }
                        }
                    }
                }) {
                    if (currentCount == numOfQues) {
                        Text(text = stringResource(R.string.save_record))
                    } else {
                        Text(text = stringResource(R.string.next))
                    }
                }
            }
        }
    )
}

