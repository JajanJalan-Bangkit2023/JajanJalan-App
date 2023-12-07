package com.bangkit.jajanjalan.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bangkit.jajanjalan.data.remote.response.LoginResponse
import com.bangkit.jajanjalan.data.remote.response.User
import com.bangkit.jajanjalan.data.remote.response.UserResponse
import com.bangkit.jajanjalan.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    private val resultLogin = MediatorLiveData<Result<LoginResponse>>()
    private val resultRegister = MediatorLiveData<Result<UserResponse>>()

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
}