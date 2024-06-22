package com.dentalcare.quiz.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orm.SugarRecord
import com.dentalcare.quiz.util.getResultFromScore
import java.io.Serializable


data class QuizResult(
    val name: String? = null,
    val number: String? = null,
    val dob: Long? = null,
    val dateOfEntry: Long? = null,
    val answers: String? = null
) : Serializable, SugarRecord<QuizResult>() {
    fun getQuesAnswers(): List<Answer> {
        val listType = object : TypeToken<List<Answer>>() {}.type
        return Gson().fromJson<List<Answer>>(answers, listType).sortedBy { it.question }
    }

    fun getMarksAchieved(): Pair<Int, String> {
        var score = 0
        getQuesAnswers().forEach {
            score += it.answer
        }
        val result = getResultFromScore(score)
        return Pair(score, result)
    }
}