package com.bangkit.jajanjalan.data.remote.retrofit

import com.bangkit.jajanjalan.data.pref.UserModel
import com.bangkit.jajanjalan.data.remote.response.ListPenjual
import com.bangkit.jajanjalan.data.remote.response.ListRecommend
import com.bangkit.jajanjalan.data.remote.response.LoginResponse
import com.bangkit.jajanjalan.data.remote.response.MenuByPenjualResponse
import com.bangkit.jajanjalan.data.remote.response.MenuResponse
import com.bangkit.jajanjalan.data.remote.response.PenjualResponse
import com.bangkit.jajanjalan.data.remote.response.User
import com.bangkit.jajanjalan.data.remote.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part("name") name: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part,
    ) : UserResponse

    // Menu
    @GET("menu")
    suspend fun getAllMenu(): Response<MenuResponse>

    @GET("penjual")
    suspend fun getAllPenjual(): ListPenjual

    @GET("menu/penjual/{id}")
    suspend fun getMenuByPenjual(
        @Path("id") id: Int
    ): Response<MenuResponse>

    @GET("menu/search")
    suspend fun searchMenu(
        @Query("item") item: String
    ): MenuResponse

    @GET("recomendation/high-rating")
    suspend fun getRecommendationMenu(
        @Header("Authorization") token: String
    ): MenuResponse

    // Penjual
    @GET("penjual/{id}")
    suspend fun getPenjualDetail(
        @Path("id") id: Int
    ): Response<PenjualResponse>
}