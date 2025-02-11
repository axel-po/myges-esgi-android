package com.example.mygesandroidesgi.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mygesandroidesgi.databinding.FragmentLoginBinding
import com.example.mygesandroidesgi.domain.usecase.GetStudentGradesUseCase
import com.example.mygesandroidesgi.domain.usecase.LoginUseCase
import com.example.mygesandroidesgi.infrastructure.repositoryImpl.FirebaseGradeRepository
import com.example.mygesandroidesgi.infrastructure.repositoryImpl.FirebaseUserRepository
import com.example.mygesandroidesgi.presentation.ui.dashboard.DashboardActivity
import com.example.mygesandroidesgi.presentation.viewmodel.AuthState
import com.example.mygesandroidesgi.presentation.viewmodel.AuthViewModel
import com.example.mygesandroidesgi.presentation.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            LoginUseCase(FirebaseUserRepository(FirebaseAuth.getInstance())),
            GetStudentGradesUseCase(FirebaseGradeRepository(FirebaseFirestore.getInstance()))
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailEditText.setOnFocusChangeListener { _, _ -> hideError() }
        binding.passwordEditText.setOnFocusChangeListener { _, _ -> hideError() }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs.")
                return@setOnClickListener
            }

            authViewModel.login(email, password)
        }

        lifecycleScope.launch {
            authViewModel.loginState.collectLatest { state ->
                when (state) {
                    is AuthState.Success -> navigateToDashboard()
                    is AuthState.Error -> showError(state.message)
                    else -> Unit
                }
            }
        }
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.errorTextView.visibility = View.GONE
    }

    private fun navigateToDashboard() {
        startActivity(Intent(requireContext(), DashboardActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
