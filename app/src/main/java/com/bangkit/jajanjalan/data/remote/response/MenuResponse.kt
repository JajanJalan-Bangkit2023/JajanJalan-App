package com.bangkit.jajanjalan.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MenuResponse(

	@field:SerializedName("data")
	val data: List<DataMenuItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class MenuByPenjualResponse(

	@field:SerializedName("data")
	val data: List<MenuPenjual>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PenjualResponse(

	@field:SerializedName("data")
	val data: Penjual? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ListPenjual(

	@field:SerializedName("data")
	val data: AllPenjual? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class AllPenjual(

	@field:SerializedName("penjual")
	val penjual: List<Penjual>? = null
)


@Parcelize
data class DataMenuItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("menu")
	val menu: Menu? = null,

	@field:SerializedName("penjual")
	val penjual: Penjual? = null,

	@field:SerializedName("penjualId")
	val penjualId: Int? = null,

) : Parcelable

@Parcelize
data class Menu(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("item")
	val item: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("description")
	val description: String? = null
) : Parcelable

data class MenuPenjual(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("penjualId")
	val penjualId: Int? = null,

	@field:SerializedName("item")
	val item: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("description")
	val description: String? = null
)

@Parcelize
data class Penjual(
	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("isOpen")
	val isOpen: Boolean? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
) : Parcelable

data class Rating(

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("comment")
	val count: String? = null,

	@field:SerializedName("menu_id")
	val menuId: Int? = null
)

data class ListRecommend(

	@field:SerializedName("data")
	val data: List<DataMenuItem>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Recommend(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("penjualId")
	val penjualId: Int? = null,

	@field:SerializedName("item")
	val item: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,
)


