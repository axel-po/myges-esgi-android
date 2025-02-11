package com.example.mygesandroidesgi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygesandroidesgi.domain.usecase.GetSchedulesUseCase

class ScheduleViewModelFactory(
    private val getSchedulesUseCase: GetSchedulesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(getSchedulesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
