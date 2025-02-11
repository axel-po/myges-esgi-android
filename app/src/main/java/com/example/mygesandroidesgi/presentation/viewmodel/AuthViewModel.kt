package com.example.mygesandroidesgi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mygesandroidesgi.domain.model.Grade
import com.example.mygesandroidesgi.domain.usecase.GetStudentGradesUseCase
import com.example.mygesandroidesgi.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getStudentGradesUseCase: GetStudentGradesUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> get() = _loginState

    private val _grades = MutableStateFlow<List<Grade>>(emptyList())
    val grades: StateFlow<List<Grade>> get() = _grades

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                loginUseCase.execute(email, password).collect { user ->
                    if (user != null) {
                        _loginState.value = AuthState.Success
                        fetchGrades(user.id)
                    } else {
                        _loginState.value = AuthState.Error("Email ou mot de passe incorrect")
                    }
                }
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.localizedMessage ?: "Erreur inconnue")
            }
        }
    }

    fun fetchGrades(studentId: String) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Fetching grades for user: $studentId")
                getStudentGradesUseCase.execute(studentId).collectLatest { gradeList ->
                    Log.d("AuthViewModel", "Received ${gradeList.size} grades")
                    _grades.value = gradeList
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error fetching grades", e)
            }
        }
    }
}

class AuthViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val getStudentGradesUseCase: GetStudentGradesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(loginUseCase, getStudentGradesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
