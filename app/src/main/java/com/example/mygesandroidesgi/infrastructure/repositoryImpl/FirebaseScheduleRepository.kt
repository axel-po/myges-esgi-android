package com.example.mygesandroidesgi.infrastructure.repositoryImpl

import android.util.Log
import com.example.mygesandroidesgi.domain.model.Schedule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseScheduleRepository(private val db: FirebaseFirestore) {

    suspend fun getSchedules(): List<Schedule> {
        return try {
            Log.d("FirebaseScheduleRepo", "Fetching schedules from Firestore...")
            val snapshot = db.collection("Schedules").get().await()

            if (snapshot.isEmpty) {
                Log.w("FirebaseScheduleRepo", "⚠Aucun cours trouvé dans Firestore")
            } else {
                Log.d("FirebaseScheduleRepo", "${snapshot.documents.size} cours trouvés")
            }

            snapshot.documents.mapNotNull { doc ->
                val schedule = doc.toObject(Schedule::class.java)?.copy(id = doc.id)
                Log.d("FirebaseScheduleRepo", "Cours récupéré: $schedule")
                schedule
            }
        } catch (e: Exception) {
            Log.e("FirebaseScheduleRepo", "Erreur lors du fetch des cours : ${e.message}", e)
            emptyList()
        }
    }

}
