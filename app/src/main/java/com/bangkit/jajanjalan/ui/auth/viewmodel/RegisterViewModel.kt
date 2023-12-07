package com.bangkit.jajanjalan.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.jajanjalan.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val repository: UserRepository
): ViewModel() {

    fun register(email: String, name: String, password : String, role: String) = repository.register(email, name, password, role)
}