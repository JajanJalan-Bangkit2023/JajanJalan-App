package com.bangkit.jajanjalan.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.UserRepository
import com.bangkit.jajanjalan.data.remote.response.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

     private val resultUser: LiveData<Result<UserResponse>> get() = repository.resultUser
        fun login(email: String, password : String) = repository.login(email, password)

        fun saveUser(userId: String, email: String, name: String, image: String , password: String, token: String) {
            viewModelScope.launch {
                repository.saveUser(userId, email, name, image, password, token)
            }
        }

    fun getDetailUser(userId: String): LiveData<Result<UserResponse>> {
        viewModelScope.launch {
            repository.getDetailUser(userId)
        }
        return resultUser
    }
}