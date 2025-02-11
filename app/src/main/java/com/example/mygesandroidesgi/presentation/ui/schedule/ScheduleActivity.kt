package com.example.mygesandroidesgi.presentation.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.mygesandroidesgi.R
import com.example.mygesandroidesgi.domain.usecase.GetSchedulesUseCase
import com.example.mygesandroidesgi.infrastructure.repositoryImpl.FirebaseScheduleRepository
import com.example.mygesandroidesgi.presentation.viewmodel.ScheduleViewModel
import com.example.mygesandroidesgi.presentation.viewmodel.ScheduleViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ScheduleActivity : AppCompatActivity() {

    private val scheduleViewModel: ScheduleViewModel by viewModels {
        ScheduleViewModelFactory(GetSchedulesUseCase(FirebaseScheduleRepository(FirebaseFirestore.getInstance())))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        Log.d("ScheduleActivity", "🚀 ScheduleActivity onCreate() appelé !")

        val recyclerViewSchedules: RecyclerView = findViewById(R.id.recyclerViewSchedules)
        val noCoursesTextView: TextView =
            findViewById(R.id.noCoursesTextView)
        val adapter = ScheduleAdapter()

        recyclerViewSchedules.layoutManager = LinearLayoutManager(this)
        recyclerViewSchedules.adapter = adapter

        Log.d("ScheduleActivity", "✅ RecyclerView initialisé, lancement du fetch")

        scheduleViewModel.fetchSchedules()

        lifecycleScope.launch {
            scheduleViewModel.schedules.collectLatest { schedules ->
                Log.d("ScheduleActivity", "${schedules.size} cours reçus depuis ViewModel")

                if (schedules.isEmpty()) {
                    noCoursesTextView.visibility = TextView.VISIBLE
                    recyclerViewSchedules.visibility = RecyclerView.GONE
                    Log.w("ScheduleActivity", "⚠Aucun cours à afficher")
                } else {
                    noCoursesTextView.visibility = TextView.GONE
                    recyclerViewSchedules.visibility = RecyclerView.VISIBLE
                    adapter.submitList(schedules)
                    Log.d("ScheduleActivity", "Cours affichés dans RecyclerView")
                }
            }
        }
    }
}
