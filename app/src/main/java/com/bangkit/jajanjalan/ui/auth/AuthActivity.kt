package com.bangkit.jajanjalan.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.jajanjalan.databinding.ActivityAuthBinding
import com.bangkit.jajanjalan.ui.MainActivity
import com.bangkit.jajanjalan.ui.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUserAuthentication()
        lifecycleScope.launch {
            viewModel.user.collect { isAuthenticated ->
                // Update UI with user data
                if (isAuthenticated) {
                    navigateToMainActivity()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        Intent(this, MainActivity::class.java).also {intent->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}