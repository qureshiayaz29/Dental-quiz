package com.dentalcare.quiz.model

import java.io.Serializable

data class Answer(
    val question: Int,
    val answer: Int
): Serializable
