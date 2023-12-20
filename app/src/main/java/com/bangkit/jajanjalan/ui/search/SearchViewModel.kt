package com.bangkit.jajanjalan.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.UserRepository
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.ListRecommend
import com.bangkit.jajanjalan.data.remote.response.MenuResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (private val repository: UserRepository): ViewModel() {

    fun searchMenu(query: String) = repository.searchMenu(query)

}