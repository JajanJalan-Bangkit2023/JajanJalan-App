package com.bangkit.jajanjalan.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.jajanjalan.data.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FavDatabase: RoomDatabase() {
    abstract fun favDao(): FavoriteDao
}