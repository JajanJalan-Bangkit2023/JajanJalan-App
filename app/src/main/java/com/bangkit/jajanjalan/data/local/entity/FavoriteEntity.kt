package com.bangkit.jajanjalan.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_seller")
class FavoriteEntity (

    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "image")
    val image: String,

    @field:ColumnInfo(name = "address")
    val address: String
)