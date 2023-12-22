package com.bangkit.jajanjalan.ui.maps

import androidx.lifecycle.ViewModel
import com.bangkit.jajanjalan.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    fun getAllPenjual() = repository.getAllPenjual()
}