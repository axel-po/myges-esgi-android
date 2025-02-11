package com.example.mygesandroidesgi.presentation.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygesandroidesgi.R
import com.example.mygesandroidesgi.domain.usecase.GetStudentGradesUseCase
import com.example.mygesandroidesgi.domain.usecase.LoginUseCase
import com.example.mygesandroidesgi.infrastructure.repositoryImpl.FirebaseGradeRepository
import com.example.mygesandroidesgi.infrastructure.repositoryImpl.FirebaseUserRepository
import com.example.mygesandroidesgi.presentation.ui.login.LoginActivity
import com.example.mygesandroidesgi.presentation.ui.schedule.ScheduleActivity
import com.example.mygesandroidesgi.presentation.viewmodel.AuthViewModel
import com.example.mygesandroidesgi.presentation.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            LoginUseCase(FirebaseUserRepository(FirebaseAuth.getInstance())),
            GetStudentGradesUseCase(FirebaseGradeRepository(FirebaseFirestore.getInstance()))
        )
    }

    private lateinit var gradeAdapter: GradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val recyclerViewGrades: RecyclerView = findViewById(R.id.recyclerViewGrades)
        gradeAdapter = GradeAdapter()
        recyclerViewGrades.layoutManager = LinearLayoutManager(this)
        recyclerViewGrades.adapter = gradeAdapter

        authViewModel.fetchGrades(currentUser.uid)

        lifecycleScope.launch {
            authViewModel.grades.collect { grades ->
                gradeAdapter.submitList(grades)
            }
        }

        val scheduleButton: Button = findViewById(R.id.schedule_button)
        scheduleButton.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        val logoutButton: Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
