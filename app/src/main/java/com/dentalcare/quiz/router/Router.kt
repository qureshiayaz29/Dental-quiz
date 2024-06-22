package com.dentalcare.quiz.router

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object PatientDetails

@Serializable
data class Quiz(
    val name: String,
    val number: String,
    val dob: Long,
)

@Serializable
object Records

@Serializable
data class Success(
    val result: Int
)