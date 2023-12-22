package com.bangkit.jajanjalan.ui.favorite

import androidx.lifecycle.ViewModel
import com.bangkit.jajanjalan.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    fun getAllFavorite() = repository.getAllFavorite()
}