package com.example.mygesandroidesgi.domain.model

import com.google.firebase.Timestamp

data class Grade(
    val id: String = "",
    val studentId: String = "",
    val courseId: String = "",
    val teacherId: String = "",
    val note: Double = 0.0,
    val date: Timestamp? = null
)
