package com.dentalcare.quiz.model

import android.net.Uri
import com.google.gson.Gson
import com.orm.SugarRecord

data class PatientData(
    val name: String,
    val number: String,
    val dob: Long,
) : SugarRecord<PatientData>() {
    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
}

//class PatientDataType : NavType<PatientData>(isNullableAllowed = false) {
//    override fun get(bundle: Bundle, key: String): PatientData? {
//        return bundle.getParcelable(key)
//    }
//
//    override fun parseValue(value: String): PatientData {
//        return Gson().fromJson(value, PatientData::class.java)
//    }
//
//    override fun put(bundle: Bundle, key: String, value: PatientData) {
//        bundle.putParcelable(key, value)
//    }
//}