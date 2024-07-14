package com.dentalcare.quiz.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dentalcare.quiz.R
import com.dentalcare.quiz.model.FileSaveResponse
import com.dentalcare.quiz.util.formatReadableDate
import com.dentalcare.quiz.util.getIcon
import com.dentalcare.quiz.util.openPdfViewer
import com.dentalcare.quiz.viewmodel.AppViewModel

@ExperimentalMaterial3Api
@Composable
fun Records(
    navController: NavController,
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    val records = viewModel.getAllRecords()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.records)) },
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
                    .fillMaxHeight()
                    .fillMaxSize()
            ) {
                Text(text = stringResource(R.string.total_results, records.size))
                if (records.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 80.dp)
                    ) {
                        ShowNoDataFoundView()
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                ) {
                    items(records) {
                        RecordCard(
                            name = it.name.orEmpty(),
                            dateOfEntry = it.dateOfEntry ?: -1L,
                            onExportClicked = {
                                viewModel.savePdf(
                                    quizResult = it,
                                    logo = getIcon(context, R.drawable.logo),
                                    title = context.getString(R.string.report_title),
                                    title2 = context.getString(R.string.developed_by_university),
                                    subtitle = context.getString(R.string.dentist_name)
                                ) { response, filePath ->
                                    showResult(context, response, filePath)
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

fun showResult(context: Context, response: FileSaveResponse, filePath: String) {
    when(response) {
        FileSaveResponse.SAVED -> {
            Toast.makeText(
                context,
                context.getString(R.string.success_file_exported_successfully), Toast.LENGTH_SHORT
            ).show()
            openPdfViewer(context, filePath)
        }
        FileSaveResponse.FAILED -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_something_went_wrong), Toast.LENGTH_SHORT
            ).show()
        }
        FileSaveResponse.FILE_EXIST -> {
            openPdfViewer(context, filePath)
        }
    }
}

@Composable
fun RecordCard(
    name: String,
    dateOfEntry: Long,
    onExportClicked: () -> Unit
) {
    Card(
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatReadableDate(dateOfEntry),
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onExportClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "export"
                    )
                }
            }
        }
    }
}

@Composable
fun ShowNoDataFoundView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data_found))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = { progress },
        alignment = Alignment.Center
    )
}