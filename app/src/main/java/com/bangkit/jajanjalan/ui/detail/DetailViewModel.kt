package com.bangkit.jajanjalan.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.jajanjalan.data.Result
import com.bangkit.jajanjalan.data.UserRepository
import com.bangkit.jajanjalan.data.local.entity.FavoriteEntity
import com.bangkit.jajanjalan.data.remote.response.MenuByPenjualResponse
import com.bangkit.jajanjalan.data.remote.response.MenuResponse
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

    fun getMenuByPenjual(penjualId: Int): LiveData<Result<MenuResponse>> {
        viewModelScope.launch {
            repository.getMenuByPenjual(penjualId)
        }
        return menuByPenjual
    }

    fun addFavoriteSeller(seller: FavoriteEntity) = repository.addToFavorite(seller)
    fun deleteFavoriteSeller(seller: FavoriteEntity) = repository.deleteFromFavorite(seller)

    fun isFavorite(id: Int): LiveData<Boolean> = repository.isFavorite(id)
}