package com.example.mygesandroidesgi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygesandroidesgi.domain.model.Schedule
import com.example.mygesandroidesgi.domain.usecase.GetSchedulesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(private val getSchedulesUseCase: GetSchedulesUseCase) : ViewModel() {

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> get() = _schedules

    fun fetchSchedules() {
        viewModelScope.launch {
            Log.d("ScheduleViewModel", "⏳ Début du fetch des cours...")
            getSchedulesUseCase.execute().collect { schedulesList ->
                _schedules.value = schedulesList
                Log.d(
                    "ScheduleViewModel",
                    " ${schedulesList.size} cours récupérés et stockés dans ViewModel"
                )
            }
        }
    }

}
