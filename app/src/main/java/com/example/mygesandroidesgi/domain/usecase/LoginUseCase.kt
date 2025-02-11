package com.example.mygesandroidesgi.domain.usecase

import com.example.mygesandroidesgi.domain.model.User

import com.example.myapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: IUserRepository) {
    suspend fun execute(email: String, password: String): Flow<User?> {
        return repository.login(email, password)
    }
}
