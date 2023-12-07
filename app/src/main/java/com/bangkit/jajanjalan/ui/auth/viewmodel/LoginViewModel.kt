package com.bangkit.jajanjalan.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.jajanjalan.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

        fun login(email: String, password : String) = repository.login(email, password)
}