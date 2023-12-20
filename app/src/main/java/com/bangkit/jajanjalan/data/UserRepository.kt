package com.bangkit.jajanjalan.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.bangkit.jajanjalan.data.local.entity.FavoriteEntity
import com.bangkit.jajanjalan.data.local.room.FavoriteDao
import com.bangkit.jajanjalan.data.pref.DataStoreManager
import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.ListPenjual
import com.bangkit.jajanjalan.data.remote.response.ListRecommend
import com.bangkit.jajanjalan.data.remote.response.LoginResponse
import com.bangkit.jajanjalan.data.remote.response.MenuByPenjualResponse
import com.bangkit.jajanjalan.data.remote.response.MenuResponse
import com.bangkit.jajanjalan.data.remote.response.PenjualResponse
import com.bangkit.jajanjalan.data.remote.response.User
import com.bangkit.jajanjalan.data.remote.response.UserResponse
import com.bangkit.jajanjalan.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStoreManager,
    private val favDao: FavoriteDao
) {
    private val resultLogin = MediatorLiveData<Result<LoginResponse>>()
    private val resultRegister = MediatorLiveData<Result<UserResponse>>()
    private val _resultUser = MediatorLiveData<Result<UserResponse>>()
    val resultUser: LiveData<Result<UserResponse>> get() = _resultUser
    val _user = MediatorLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    private val _menu = MediatorLiveData<Result<MenuResponse>>()
    val menu: LiveData<Result<MenuResponse>> get() = _menu

    private val _penjualDetail = MediatorLiveData<Result<PenjualResponse>>()
    val penjualDetail: LiveData<Result<PenjualResponse>> get() = _penjualDetail

    private val _menuByPenjual = MediatorLiveData<Result<MenuByPenjualResponse>>()
    val menuByPenjual: LiveData<Result<MenuByPenjualResponse>> get() = _menuByPenjual

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        resultLogin.value = Result.Loading
        apiService.login(User(email = email, password = password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()!!
                    resultLogin.value = Result.Success(responseData)
                } else {
                    resultLogin.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resultLogin.value = Result.Error(t.message.toString())
            }
        })
        return resultLogin
    }

    fun register(email: String, name: String, password: String, role: String): LiveData<Result<UserResponse>> {
        resultRegister.value = Result.Loading
        apiService.register(User(email = email, name = name, password = password, role = role)).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()!!
                    resultRegister.value = Result.Success(responseData)
                } else {
                    resultRegister.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                resultRegister.value = Result.Error(t.message.toString())
            }
        })
        return resultRegister
    }

    suspend fun getDetailUser(userId: String): LiveData<Result<UserResponse>> {
//        apiService.getUserDetail(userId).enqueue(object : Callback<UserResponse> {
//            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
//                if (response.isSuccessful) {
//                    val responseData = response.body()
//                    _resultUser.value = Result.Success(responseData!!)
//                } else {
//                    _resultUser.value = Result.Error(response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                _resultUser.value = Result.Error(t.message.toString())
//            }
//        })
//        return _resultUser
        val response = apiService.getUserDetail(userId)
        if (response.isSuccessful) {
            val responseData = response.body()
            _resultUser.value = Result.Success(responseData!!)
        } else {
            _resultUser.value = Result.Error(response.message())
        }
        return _resultUser
    }

    fun updateProfile(
        token: String,
        id: String,
        name: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part
    ) : LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updateUser(token, id, name, password, image)
            if (response.message == "User updated successfully") {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message!!))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, UserResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error("Upload failed: $errorMessage")
        }
    }


    suspend fun saveUser(userId: String, email: String, name: String, image: String , password: String, token: String) {
        dataStore.saveUser(userId, email, name, image, password, token)
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.getUser
    }

    suspend fun clear() {
        dataStore.clear()
    }

    // Menu
    suspend fun getAllMenu(): LiveData<Result<MenuResponse>> {
        _menu.value = Result.Loading
        val response = apiService.getAllMenu()
        Log.d("GET Menu", "GET All Menu")
        if (response.isSuccessful) {
            val responseData = response.body()
            _menu.value = Result.Success(responseData!!)
            Log.d("Menu Response", responseData.toString())
        } else {
            _menu.value = Result.Error(response.message())
        }
        return _menu
    }

    fun getAllPenjual() : LiveData<Result<ListPenjual>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllPenjual()
            if (response.status == "success") {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.status!!))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, PenjualResponse::class.java)
            val errorMessage = errorBody.status
            Result.Error("Upload failed: $errorMessage")
        }
    }

    suspend fun getMenuByPenjual(id: Int): LiveData<Result<MenuByPenjualResponse>> {
        _menuByPenjual.value = Result.Loading
        val response = apiService.getMenuByPenjual(id)
        if (response.isSuccessful) {
            val responseData = response.body()
            _menuByPenjual.value = Result.Success(responseData!!)
        } else {
            _menuByPenjual.value = Result.Error(response.message())
        }
        return _menuByPenjual
    }

    fun getRecommend(token: String) : LiveData<Result<MenuResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getRecommendationMenu(token)
            if (response.status == "success") {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.status!!))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, PenjualResponse::class.java)
            val errorMessage = errorBody.status
            Result.Error("Get Recommendation Failed: $errorMessage")
        }
    }

    // Penjual
    suspend fun getPenjualDetail(id: Int): LiveData<Result<PenjualResponse>> {
        _penjualDetail.value = Result.Loading
        val response = apiService.getPenjualDetail(id)
        if (response.isSuccessful) {
            val responseData = response.body()
            _penjualDetail.value = Result.Success(responseData!!)
        } else {
            _penjualDetail.value = Result.Error(response.message())
        }
        return _penjualDetail
    }

    // Favorite
    fun getAllFavorite() : LiveData<Result<List<FavoriteEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = favDao.getFavorite()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun isFavorite(id: Int) : LiveData<Boolean> {
        return favDao.isFavorite(id)
    }

    fun addToFavorite(favoriteEntity: FavoriteEntity) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            favDao.insertFavorite(favoriteEntity)
            emit(Result.Success("Seller added to favorite!"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun deleteFromFavorite(favoriteEntity: FavoriteEntity) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            favDao.deleteFavorite(favoriteEntity)
            emit(Result.Success("Seller deleted from favorite!"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }


}