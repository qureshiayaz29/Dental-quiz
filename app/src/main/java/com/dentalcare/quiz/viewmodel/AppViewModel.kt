package com.dentalcare.quiz.viewmodel

import android.graphics.drawable.Drawable
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dentalcare.quiz.analytics.Analytics
import com.dentalcare.quiz.model.Answer
import com.dentalcare.quiz.model.FileSaveResponse
import com.dentalcare.quiz.model.QuizResult
import com.dentalcare.quiz.util.addDivider
import com.dentalcare.quiz.util.addEmptyLine
import com.dentalcare.quiz.util.addImage
import com.dentalcare.quiz.util.addSubHeading
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

    fun insertQuizResult(
        name: String,
        number: String,
        dob: Long,
        quizAnswers: Map<Int, Int>,
        onResultSaved: (Pair<Int, String>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val answers = mutableListOf<Answer>()
            quizAnswers.forEach { (quesNo, answer) ->
                answers.add(Answer(quesNo, answer))
            }
            val quizResult = QuizResult(
                name = name,
                number = number,
                dob = dob,
                dateOfEntry = System.currentTimeMillis(),
                answers = Gson().toJson(answers).toString()
            )
            quizResult.save()
            withContext(Dispatchers.Main) {
                onResultSaved.invoke(quizResult.getMarksAchieved())
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
        subtitle: String,
        onFileSaveCompleted: (FileSaveResponse, String) -> Unit
    ) {
        viewModelScope.launch {
            val folder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath + "/mrdental/"
            with(File(folder)) {
                if (exists().not()) {
                    mkdir()
                }
            }
            val pdfFilePath = folder + "${quizResult.name?.replace(" ", "-")?.trim()}-${getFileName(quizResult.dateOfEntry)}"
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
                    addSubHeading(subtitle)
                    addEmptyLine()
                    addText(
                        "Patient Details: \n" +
                                "Name: ${quizResult.name}\n" +
                                "Number: ${quizResult.number}\n" +
                                "DOB: ${formatDate(quizResult.dob ?: 0)}"
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
        dob: Long?
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
        } else if (dob == null) {
            "Select date of birth"
        } else {
            null
        }
    }

    fun getMissingAnswer(quesAnswered: MutableSet<Int>, numOfQues: Int): Int {
        val allQuestions = (1 .. numOfQues).toList()
        return allQuestions.minus(quesAnswered.toList().toSet()).first()
    }
}
