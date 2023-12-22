package com.bangkit.jajanjalan.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.bangkit.jajanjalan.data.local.room.FavDatabase
import com.bangkit.jajanjalan.data.pref.DataStoreManager
import com.bangkit.jajanjalan.data.remote.retrofit.ApiService
import com.bangkit.jajanjalan.util.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://jajanjalan-api-wt3sy4qeta-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDataStorePreference(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun provideDataStoreManager(dataStore: DataStore<Preferences>) : DataStoreManager {
        return DataStoreManager(dataStore)
    }

    @Singleton
    @Provides
    fun provideFavDatabase(application: Application) : FavDatabase {
        return Room.databaseBuilder(
            application,
            FavDatabase::class.java,
            "FavDatabase.db"
        ).build()
    }

    @Provides
    fun provideFavDao(favDatabase: FavDatabase) = favDatabase.favDao()
}