package io.dev.pace_app_mobile.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun saveUniversityId(universityId: Long) {
        prefs.edit().putLong("university_id", universityId).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun getUniversityId(): Long? {
        return prefs.getLong("university_id", 0L)
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }
}