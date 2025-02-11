package com.example.mygesandroidesgi.domain.usecase

import com.example.mygesandroidesgi.domain.model.Grade
import com.example.mygesandroidesgi.domain.repository.IGradeRepository
import kotlinx.coroutines.flow.Flow

class GetStudentGradesUseCase(private val repository: IGradeRepository) {
    suspend fun execute(studentId: String): Flow<List<Grade>> {
        return repository.getGradesByStudentId(studentId)
    }
}
