package com.example.mygesandroidesgi.domain.model

import com.google.firebase.Timestamp

data class Schedule(
    val id: String = "",
    val classId: String = "",
    val courseId: String = "",
    val date: Timestamp? = null,
    val startTime: String = "",
    val endTime: String = ""
)
