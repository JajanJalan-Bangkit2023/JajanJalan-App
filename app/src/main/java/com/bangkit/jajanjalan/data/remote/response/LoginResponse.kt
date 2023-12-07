package com.bangkit.jajanjalan.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("userToken")
	val userToken: UserToken? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserToken(

	@field:SerializedName("token")
	val token: String? = null
)
