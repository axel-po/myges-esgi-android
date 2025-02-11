package com.example.mygesandroidesgi.domain.repository

import com.example.mygesandroidesgi.domain.model.Grade
import kotlinx.coroutines.flow.Flow

interface IGradeRepository {
    suspend fun getGradesByStudentId(studentId: String): Flow<List<Grade>>
}
