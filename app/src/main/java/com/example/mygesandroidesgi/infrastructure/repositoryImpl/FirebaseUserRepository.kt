package com.example.mygesandroidesgi.infrastructure.repositoryImpl

import com.example.myapp.domain.repository.IUserRepository
import com.example.mygesandroidesgi.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(private val auth: FirebaseAuth) : IUserRepository {

    override suspend fun login(email: String, password: String): Flow<User?> = flow {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {
                emit(
                    User(
                        it.uid,
                        it.email ?: "",
                        "Etudiant"
                    )
                )
            }
        } catch (e: Exception) {
            emit(null)
        }
    }

    override suspend fun register(email: String, password: String, role: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}
