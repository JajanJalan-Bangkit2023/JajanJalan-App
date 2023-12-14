package com.bangkit.jajanjalan.data.remote.retrofit

import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.LoginResponse
import com.bangkit.jajanjalan.data.remote.response.MenuByPenjualResponse
import com.bangkit.jajanjalan.data.remote.response.MenuResponse
import com.bangkit.jajanjalan.data.remote.response.PenjualResponse
import com.bangkit.jajanjalan.data.remote.response.User
import com.bangkit.jajanjalan.data.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // User
    @POST("users/register")
    fun register(
        @Body user: User
    ): Call<UserResponse>

    @POST("users/login")
    fun login(
        @Body user: User
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users/logout")
    fun logout(
        @Field("user_id") userId: String,
    )

    @GET("users/{id}")
    suspend fun getUserDetail(
        @Path("id") id: String
    ): Response<UserResponse>

    @Multipart
    @PATCH("users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Part("name") name: String,
        @Part("password") password: String,
        @Part("image") image: MultipartBody.Part,
    )

    // Menu
    @GET("menu")
    suspend fun getAllMenu(): Response<MenuResponse>

    @GET("menu/penjual/{id}")
    suspend fun getMenuByPenjual(
        @Path("id") id: Int
    ): Response<MenuByPenjualResponse>

    // Penjual
    @GET("penjual/{id}")
    suspend fun getPenjualDetail(
        @Path("id") id: Int
    ): Response<PenjualResponse>
}