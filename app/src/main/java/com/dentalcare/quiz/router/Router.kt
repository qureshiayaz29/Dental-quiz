package com.dentalcare.quiz.router

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object PatientDetails

@Serializable
object Quiz

@Serializable
object Records

@Serializable
data class Success(
    val result: Int
)