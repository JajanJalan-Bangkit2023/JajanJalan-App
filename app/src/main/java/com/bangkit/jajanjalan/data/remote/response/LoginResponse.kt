package com.bangkit.jajanjalan.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val userInfo: UserInfoLogin? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserInfoLogin(

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
