package com.dentalcare.quiz.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dentalcare.quiz.R
import com.dentalcare.quiz.model.PatientData
import com.dentalcare.quiz.router.Quiz
import com.dentalcare.quiz.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetails(
    navController: NavController,
    viewModel: AppViewModel
) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var genderIsMale by remember { mutableStateOf(true) }
    val dateOfBirthState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.patient_details_title)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.enter_details),
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // name of patient
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text(text = stringResource(R.string.name_of_patient))
                    },
                    singleLine = true,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                )
                Spacer(modifier = Modifier.padding(4.dp))

                // phone number
                TextField(
                    value = number,
                    onValueChange = {
                        if (it.length <= 10) {
                            number = it
                        }
                    },
                    label = {
                        Text(text = stringResource(R.string.phone_number))
                    },
                    prefix = {
                        Text(text = stringResource(R.string.country_code))
                    },
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = "Gender",
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                )
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text =
                            if (genderIsMale) {
                                stringResource(R.string.male)
                            } else {
                                stringResource(R.string.female)
                            },
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                    Switch(
                        checked = genderIsMale,
                        onCheckedChange = {
                            genderIsMale = it
                        },
                        thumbContent =
                            if (genderIsMale) {
                                {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_male),
                                        contentDescription = "male",
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_female),
                                        contentDescription = "female",
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            },
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                // age
                TextField(
                    value = age,
                    onValueChange = {
                        age = it
                    },
                    label = {
                        Text(text = stringResource(R.string.age))
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.padding(4.dp))

                // occupation
                TextField(
                    value = occupation,
                    onValueChange = {
                        occupation = it
                    },
                    label = {
                        Text(text = stringResource(R.string.occupation))
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                Spacer(modifier = Modifier.padding(4.dp))

                // address
                TextField(
                    value = address,
                    onValueChange = {
                        address = it
                    },
                    label = {
                        Text(text = stringResource(R.string.address))
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    maxLines = 4,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )

                Spacer(modifier = Modifier.padding(8.dp))

                // date of birth
                DatePicker(
                    state = dateOfBirthState,
                    title = {
                        Text(
                            stringResource(R.string.select_date_of_birth),
                            modifier = Modifier.padding(8.dp),
                        )
                    },
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = {
                    viewModel
                        .getErrorIfAny(
                            name,
                            number,
                            age,
                            occupation,
                            address,
                            dateOfBirthState.selectedDateMillis,
                        )?.let { errorMsg ->
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                    viewModel.patientData = PatientData(
                        name = name,
                        number = number,
                        age = age.toInt(),
                        gender = if (genderIsMale) {
                            context.getString(R.string.male)
                        } else {
                            context.getString(R.string.female)
                        },
                        occupation = occupation,
                        address = address,
                        dob = dateOfBirthState.selectedDateMillis!!
                    )
                    navController.navigate(Quiz)
                }) {
                    Text(text = stringResource(R.string.continue_str))
                    Spacer(modifier = Modifier.padding(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "done",
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        },
    )

    BackHandler {
        viewModel.patientData = null
    }
}
