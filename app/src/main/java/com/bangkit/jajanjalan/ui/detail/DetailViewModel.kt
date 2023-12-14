package com.bangkit.jajanjalan.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.UserRepository
import com.bangkit.jajanjalan.data.remote.response.MenuByPenjualResponse
import com.bangkit.jajanjalan.data.remote.response.PenjualResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor (private val repository: UserRepository): ViewModel() {

    private val penjualDetail = repository.penjualDetail
    private val menuByPenjual = repository.menuByPenjual

    fun getPenjual(penjualId: Int): LiveData<Result<PenjualResponse>> {
        viewModelScope.launch {
            repository.getPenjualDetail(penjualId)
        }
        return penjualDetail
    }

    fun getMenuByPenjual(penjualId: Int): LiveData<Result<MenuByPenjualResponse>> {
        viewModelScope.launch {
            repository.getMenuByPenjual(penjualId)
        }
        return menuByPenjual
    }

}