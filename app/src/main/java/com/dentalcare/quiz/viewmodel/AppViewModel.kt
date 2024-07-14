package com.dentalcare.quiz.viewmodel

import android.graphics.drawable.Drawable
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dentalcare.quiz.analytics.Analytics
import com.dentalcare.quiz.model.Answer
import com.dentalcare.quiz.model.FileSaveResponse
import com.dentalcare.quiz.model.PatientData
import com.dentalcare.quiz.model.QuizResult
import com.dentalcare.quiz.util.addDivider
import com.dentalcare.quiz.util.addEmptyLine
import com.dentalcare.quiz.util.addImage
import com.dentalcare.quiz.util.addSubTitle
import com.dentalcare.quiz.util.addText
import com.dentalcare.quiz.util.addTitle
import com.dentalcare.quiz.util.formatDate
import com.dentalcare.quiz.util.formatReadableDate
import com.dentalcare.quiz.util.getFileName
import com.dentalcare.quiz.util.getQuestion
import com.google.gson.Gson
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.orm.SugarRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

class AppViewModel : ViewModel() {
    var patientData: PatientData? = null

    fun insertQuizResult(
        quizAnswers: Map<Int, Int>,
        onGetResult: (Pair<Int, String>?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val answers = mutableListOf<Answer>()
            quizAnswers.forEach { (quesNo, answer) ->
                answers.add(Answer(quesNo, answer))
            }
            patientData?.let {
                val quizResult =
                    QuizResult(
                        name = it.name,
                        number = it.number,
                        age = it.age,
                        gender = it.gender,
                        occupation = it.occupation,
                        address = it.address,
                        dob = it.dob,
                        dateOfEntry = System.currentTimeMillis(),
                        answers = Gson().toJson(answers).toString(),
                    )
                quizResult.save()
                withContext(Dispatchers.Main) {
                    // success case
                    onGetResult.invoke(quizResult.getMarksAchieved())
                }
            } ?: run {
                withContext(Dispatchers.Main) {
                    // failure case
                    onGetResult.invoke(null)
                }
            }
        }
    }

    fun getAllRecords(): List<QuizResult> {
        val result = mutableListOf<QuizResult>()
        SugarRecord.findAll(QuizResult::class.java).apply {
            forEachRemaining {
                result.add(it)
            }
        }
        return result.sortedByDescending { it.dateOfEntry }
    }

    fun savePdf(
        quizResult: QuizResult,
        logo: Drawable?,
        title: String,
        title2: String,
        subtitle: String,
        onFileSaveCompleted: (FileSaveResponse, String) -> Unit,
    ) {
        viewModelScope.launch {
            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath + "/mrdental/"
            with(File(folder)) {
                if (exists().not()) {
                    mkdir()
                }
            }
            val pdfFilePath =
                folder + "${
                    quizResult.name?.replace(" ", "-")?.trim()
                }-${getFileName(quizResult.dateOfEntry)}"
            runCatching {
                val pdfFile = File(pdfFilePath)
                if (pdfFile.exists()) {
                    onFileSaveCompleted.invoke(FileSaveResponse.FILE_EXIST, pdfFilePath)
                    return@launch
                }
                val fOut = FileOutputStream(pdfFilePath)
                val pdfWriter = PdfWriter(fOut)
                val pdfDocument = PdfDocument(pdfWriter)
                val layoutDocument = Document(pdfDocument)
                with(layoutDocument) {
                    addImage(logo, true)
                    addTitle(title)
                    addSubTitle(title2)
                    addSubTitle(subtitle)
                    addEmptyLine()
                    addText("Patient Details:", isUnderline = true)
                    addText(
                        "Name: ${quizResult.name}\n" +
                            "Number: ${quizResult.number}\n" +
                            "DOB: ${formatDate(quizResult.dob ?: 0)}\n" +
                            "Age: ${quizResult.age}\n" +
                            "Gender: ${quizResult.gender}\n" +
                            "Occupation: ${quizResult.occupation}\n" +
                            "Address: ${quizResult.address}",
                    )
                    addEmptyLine()
                    addDivider()
                    addEmptyLine()
                    quizResult.getMarksAchieved().let {
                        addText("Total marks achieved: ${it.first}")
                        addText("Result: ${it.second}")
                    }
                    addDivider()
                    addEmptyLine()
                    quizResult.getQuesAnswers().forEach { allQuesWithAnswers ->
                        getQuestion(allQuesWithAnswers.question).let { question ->
                            addEmptyLine()
                            addText("${allQuesWithAnswers.question}: " + question.question + " [Marks - ${allQuesWithAnswers.answer}]")
                            question.option.forEachIndexed { index, option ->
                                val symbol =
                                    if (index == allQuesWithAnswers.answer) "> " else "[x] "
                                addText(symbol + option)
                            }
                            addEmptyLine()
                            addDivider(isDotted = true)
                        }
                    }
                    addEmptyLine()
                    addText("Report generated on: ${formatReadableDate(System.currentTimeMillis())}")
                }
                layoutDocument.close()
                onFileSaveCompleted.invoke(FileSaveResponse.SAVED, pdfFilePath)
            }.onFailure {
                onFileSaveCompleted.invoke(FileSaveResponse.FAILED, "")
                Analytics.recordException(it)
            }
        }
    }

    fun getErrorIfAny(
        name: String,
        number: String,
        age: String,
        occupation: String,
        address: String,
        dob: Long?,
    ): String? {
        val pattern: Pattern = Pattern.compile("[^A-Za-z ]")
        val matcher: Matcher = pattern.matcher(name)
        val isInvalidName = matcher.find()
        return if (name.isEmpty()) {
            "Enter name"
        } else if (isInvalidName) {
            "Invalid name"
        } else if (number.isEmpty()) {
            "Enter phone number"
        } else if (age.isEmpty()) {
            "Enter age"
        } else if (age.toInt() > 99) {
            "Enter age between [0 to 99]"
        } else if (occupation.isEmpty()) {
            "Enter occupation"
        } else if (address.isEmpty()) {
            "Enter address"
        } else if (dob == null) {
            "Select date of birth"
        } else {
            null
        }
    }

    fun getMissingAnswer(
        quesAnswered: MutableSet<Int>,
        numOfQues: Int,
    ): Int {
        val allQuestions = (1..numOfQues).toList()
        return allQuestions.minus(quesAnswered.toList().toSet()).first()
    }
}
