package io.dev.pace_app_mobile.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("dynamic_link_prefs")

class DynamicLinkDataStore(private  val context: Context) {
    private val gson = Gson()
    private val DYNAMIC_LINK_KEY = stringPreferencesKey("dynamic_link")

    suspend fun saveDynamicLink(data: SharedDynamicLink) {
        val json = gson.toJson(data)
        context.dataStore.edit { prefs ->
            prefs[DYNAMIC_LINK_KEY] = json
        }
    }

    fun getDynamicLink(): Flow<SharedDynamicLink?> {
        return context.dataStore.data.map { prefs ->
            prefs[DYNAMIC_LINK_KEY]?.let { json ->
                gson.fromJson(json, SharedDynamicLink::class.java)
            }
        }
    }

    suspend fun updateVerification(isVerified: Boolean) {
        val current = getDynamicLink().map { it }.firstOrNull()
        current?.let {
            saveDynamicLink(it.copy(isVerified = isVerified))
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}