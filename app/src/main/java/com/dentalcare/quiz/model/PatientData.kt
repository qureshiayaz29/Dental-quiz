package com.dentalcare.quiz.model

import android.net.Uri
import com.google.gson.Gson
import com.orm.SugarRecord
import kotlinx.serialization.Serializable

@Serializable
data class PatientData(
    val name: String,
    val number: String,
    val age: Int,
    val gender: String,
    val occupation: String,
    val address: String,
    val dob: Long,
) : SugarRecord<PatientData>() {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}