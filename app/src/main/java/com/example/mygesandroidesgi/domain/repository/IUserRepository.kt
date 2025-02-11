package com.example.myapp.domain.repository

import com.example.mygesandroidesgi.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun login(email: String, password: String): Flow<User?>
    suspend fun register(email: String, password: String, role: String): Flow<Boolean>
}
