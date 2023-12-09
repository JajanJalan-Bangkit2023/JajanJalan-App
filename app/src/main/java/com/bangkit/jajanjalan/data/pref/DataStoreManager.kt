package com.bangkit.jajanjalan.data.pref

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_IMAGE = stringPreferencesKey("user_image")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    suspend fun saveUser(userId: String, email: String, name: String, image: String , password: String, token: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = name
            preferences[USER_IMAGE] = image
            preferences[USER_PASSWORD] = password
            preferences[USER_TOKEN] = token
        }
    }

    val getUser: Flow<UserModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("DataStoreManager", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val userId = preferences[USER_ID] ?: ""
            val email = preferences[USER_EMAIL] ?: ""
            val name = preferences[USER_NAME] ?: ""
            val image = preferences[USER_IMAGE] ?: ""
            val password = preferences[USER_PASSWORD] ?: ""
            val token = preferences[USER_TOKEN] ?: ""
            UserModel(userId, email, name, image, password, token)
        }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}