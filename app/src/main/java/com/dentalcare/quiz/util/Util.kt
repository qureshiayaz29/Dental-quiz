package com.dentalcare.quiz.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.dentalcare.quiz.R
import com.dentalcare.quiz.model.Question
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import java.text.SimpleDateFormat
import java.util.Locale

val dateTimeReadableFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa", Locale.ENGLISH)
val dateTimeFormat = SimpleDateFormat("dd-MM-yy-HH-mm-ss", Locale.ENGLISH)
val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

// TODO this should be fetched from server once, and then from local storage
fun getQuestion(index: Int): Question {
    return when (index) {
        1 -> {
            Question(
                question = "Patient’s Expectation:",
                option = listOf(
                    "Minimal Expectations",
                    "Minimal Expectation with Mastication",
                    "Average Expectation with Mastication and Phonetics",
                    "High Expectations with Mastication, Phonetics and Aesthetics"
                )
            )
        }

        2 -> {
            Question(
                question = "Psychological attitude of patient:",
                option = listOf(
                    "Patient is self-motivated, shows very positive attitude and trust towards dentist and expresses his desire to get the treatment.",
                    "Patient is motivated after treatment planning explanation, shows positive attitude and trust towards dentist and expresses his desire to get the treatment.",
                    "Patient is confused after explanation of treatment planning, shows negative attitude.",
                    "Patient is demotivated and is not willing for treatment after treatment planning explanation, shows very negative attitude and distrust towards dentist and is not much concerned about oral health."
                )
            )
        }

        3 -> {
            Question(
                question = "Smoking:",
                option = listOf(
                    "Non-Smoker-0 cigarettes per day",
                    "Light Smoker-1-9 cigarettes per day",
                    "Moderately Heavy Smoker- 16-20 cigarettes per day",
                    "Excessive Smoker-21-35 or more cigarettes per day"
                )
            )
        }

        4 -> {
            Question(
                question = "Betel Nut Chewer:",
                option = listOf(
                    "Non-chewer-0 times a day",
                    "Ocaasional chewer- 1- 2 times a day",
                    "Regular chewer- 4-6 times a day",
                    "Heavy chewer- 10 or more times a day"
                )
            )
        }

        5 -> {
            Question(
                question = "Diabetic:",
                option = listOf(
                    "Non-diabetic- Below 5.7%",
                    "Pre-diabetic- 5.7-6.5%",
                    "Controlled diabetic- Between 6.5%-7%",
                    "Dangerous level- 9% and higher"
                )
            )
        }

        6 -> {
            Question(
                question = "Anticoagulant:",
                option = listOf(
                    "Normal patient-INR 1.0",
                    "Patient on anti-coagulant- INR is b/w 2.0-3.0",
                    "Patient on anti-coagulant- INR is b/w 3-4.9",
                    "Critical Values- INR Above 4.9"
                )
            )
        }

        7 -> {
            Question(
                question = "Vitamin D:",
                option = listOf(
                    "Vitamin D sufficiency-75 to 250nM",
                    "Vitamin D optimal range-75 to 100nM",
                    "Vitamin D insufficiency-50 to 74nM",
                    "Vitamin D deficiency-<50nM"
                )
            )
        }

        8 -> {
            Question(
                question = "Family Socioeconomic Status:",
                option = listOf(
                    "More than 10lakhs",
                    "Between 08-10 lakhs",
                    "Between 6-08 lakhs",
                    "Less than 6lakhs"
                )
            )
        }

        9 -> {
            Question(
                question = "TMJ Examination:",
                option = listOf(
                    "Synchronized movement with no clicking, crepitus and deviation",
                    "Synchronized movement with clicking sound observed",
                    "Clicking and crepitus is observed but no deviation is observed",
                    "Clicking, crepitus and deviation is observed"
                )
            )
        }

        10 -> {
            Question(
                question = "Mouth Opening:",
                option = listOf(
                    "More than 35mm",
                    "Between 25-35mm",
                    "Between 15-25mm",
                    "Between 02-15mm"
                )
            )
        }

        11 -> {
            Question(
                question = "Extraction:",
                option = listOf(
                    "No Extraction Required",
                    "Extraction required in mandibular arch",
                    "Extraction required in maxillary arch",
                    "Extraction required in both maxillary and mandibular arch"
                )
            )
        }

        12 -> {
            Question(
                question = "Ridge Relation/Jaw Relation:",
                option = listOf(
                    "Class I",
                    "Class II",
                    "Class III",
                    "Abnormal Jaw Relation"
                )
            )
        }

        13 -> {
            Question(
                question = "Movable Mucosa:",
                option = listOf(
                    "3-4mm away from crest",
                    "2-3mm away from crest",
                    "1mm away from crest",
                    "At the crest "
                )
            )
        }

        14 -> {
            Question(
                question = "Ridge Parallelism:",
                option = listOf(
                    "Both ridges are parallel to the occlusal plane",
                    "The mandibular ridge is divergent from the occlusal plane anteriorly",
                    "The maxillary ridge is divergent from occlusal plane anteriorly",
                    "Both ridges are divergent from occlusal plane anteriorly"
                )
            )
        }

        15 -> {
            Question(
                question = "Muscle Tone:",
                option = listOf(
                    "The patient exhibits normal tension, tone, and placement of the muscles of mastication and facial expression",
                    "The patient displays approximately normal function slightly impaired muscle tone",
                    "The patient exhibits greatly impaired muscle tone and function.  This impairment usually is coupled with poor health, inefficient dentures.",
                    "The patient exhibits loss of vertical dimension, wrinkles, decreased biting force, and drooping commissures"
                )
            )
        }

        16 -> {
            Question(
                question = "Bone Type:",
                option = listOf(
                    "Dense Cortical",
                    "Porous cortical and coarse trabecular",
                    "Porous cortical and fine trabecular",
                    "Fine Trabecular"
                )
            )
        }

        17 -> {
            Question(
                question = "Distance of implant from maxillary sinus in maxillary arch:",
                option = listOf(
                    "More than 8mm",
                    "Between 8-10mm",
                    "Between 6-8mm",
                    "Between 4-6 mm"
                )
            )
        }

        18 -> {
            Question(
                question = "Distance of implant from Inferior alveolar nerve in mandibular arch:",
                option = listOf(
                    "More than 8mm",
                    "Between 8-10mm",
                    "Between 6-8mm",
                    "Between 4-6 mm"
                )
            )
        }

        19 -> {
            Question(
                question = "Zygomatic/Pterygoid Implant Placement:",
                option = listOf(
                    "Not required",
                    "Pterygoid implant required in one quadrant",
                    "Pterygoid implant required in both qudrants",
                    "Both zygomatic and pterygoid implant placement is required"
                )
            )
        }

        20 -> {
            Question(
                question = "Bone grafting:",
                option = listOf(
                    "Not required",
                    "1-2 cc is required",
                    "2-5cc is required",
                    "More than 5cc is required"
                )
            )
        }

        else -> Question(question = "NA", option = listOf())
    }
}

fun openPdfViewer(context: Context, filepath: String) {
    val intent = PdfViewerActivity.launchPdfFromPath(
        context = context,
        path = filepath,
        pdfTitle = context.getString(R.string.app_name),
        saveTo = saveTo.ASK_EVERYTIME,
        fromAssets = false
    )
    context.startActivity(intent)
}


fun getFileName(dateOfEntry: Long?): String {
    return "${dateTimeFormat.format(dateOfEntry)}.pdf"
}

fun formatDate(timeInMillis: Long): String {
    return dateFormat.format(timeInMillis)
}

fun formatReadableDate(timeInMillis: Long): String {
    return dateTimeReadableFormat.format(timeInMillis)
}

fun getIcon(context: Context, resId: Int): Drawable? {
    return ContextCompat.getDrawable(context, resId)
}

fun getResultFromScore(score: Int): String {
    return when(score) {
        in 0..15 -> "Very Easy"
        in 15..30 -> "Easy"
        in 31..45 -> "Difficult"
        in 45..60 -> "Very Difficult"
        else -> "NA"
    }
}