package com.example.mygesandroidesgi.domain.usecase

import com.example.mygesandroidesgi.domain.model.Schedule
import com.example.mygesandroidesgi.infrastructure.repositoryImpl.FirebaseScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSchedulesUseCase(private val repository: FirebaseScheduleRepository) {

    fun execute(): Flow<List<Schedule>> = flow {
        emit(repository.getSchedules())
    }
}
