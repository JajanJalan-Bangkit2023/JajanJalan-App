package com.bangkit.jajanjalan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.UserRepository
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.MenuResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (private val repository: UserRepository): ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val menuResponse = repository.menu

    fun getUser() {
        viewModelScope.launch {
            repository.getUser()
                .collect { user ->
                    _user.value = user
                }
        }
    }

    fun getAllMenu(): LiveData<Result<MenuResponse>> {
        viewModelScope.launch {
            repository.getAllMenu()
        }
        return menuResponse
    }
}