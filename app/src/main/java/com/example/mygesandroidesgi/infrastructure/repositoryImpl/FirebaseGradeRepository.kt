package com.example.mygesandroidesgi.infrastructure.repositoryImpl

import android.util.Log
import com.example.mygesandroidesgi.domain.model.Grade
import com.example.mygesandroidesgi.domain.repository.IGradeRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseGradeRepository(private val db: FirebaseFirestore) : IGradeRepository {

    override suspend fun getGradesByStudentId(studentId: String): Flow<List<Grade>> = flow {
        try {
            Log.d("FirebaseGradeRepository", "Fetching grades for studentId: $studentId")

            val snapshot = db.collection("Grades")
                .whereEqualTo("studentId", studentId)
                .get()
                .await()

            val grades = snapshot.documents.mapNotNull { document ->
                try {
                    val grade = document.toObject(Grade::class.java)
                    val timestamp = document.getTimestamp("date")

                    Log.d(
                        "FirebaseGradeRepository",
                        "Document ID: ${document.id}, Date: $timestamp"
                    )

                    grade?.copy(date = timestamp)

                } catch (e: Exception) {
                    Log.e("FirebaseGradeRepository", "Error converting document ${document.id}", e)
                    null
                }
            }

            Log.d(
                "FirebaseGradeRepository",
                "Fetched ${grades.size} grades: $grades"
            )
            emit(grades)

        } catch (e: Exception) {
            Log.e("FirebaseGradeRepository", "Error fetching grades", e)
            emit(emptyList())
        }
    }
}
