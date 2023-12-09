package com.bangkit.jajanjalan.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.jajanjalan.data.UserRepository
import com.bangkit.jajanjalan.data.pref.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    fun getUser() {
        viewModelScope.launch {
            repository.getUser()
                .collect { user ->
                    _user.value = user
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clear()
        }
    }
}