package io.dev.pace_app_mobile.data.local.datastore

import android.R
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("dynamic_link_prefs")

class LocalDataStore(private val context: Context) {
    private val gson = Gson()
    private val DYNAMIC_LINK_KEY = stringPreferencesKey("dynamic_link")
    private val VERIFIED_ACCOUNT_KEY = stringPreferencesKey("verified_account")

    private val GUEST_KEY = stringPreferencesKey("guest_key")

    suspend fun saveDynamicLink(data: SharedDynamicLink) {
        val json = gson.toJson(data)
        context.dataStore.edit { prefs ->
            prefs[DYNAMIC_LINK_KEY] = json
        }
    }

    suspend fun saveGuestKey(message: String) {
        val json = gson.toJson(message)
        context.dataStore.edit { prefs ->
            prefs[GUEST_KEY] = json
        }
    }

    suspend fun saveVerifiedAccount(data: SharedVerifiedAccount) {
        val json = gson.toJson(data)
        context.dataStore.edit { prefs ->
            prefs[VERIFIED_ACCOUNT_KEY] = json
        }
    }

    fun getDynamicLink(): Flow<SharedDynamicLink?> {
        return context.dataStore.data.map { prefs ->
            prefs[DYNAMIC_LINK_KEY]?.let { json ->
                gson.fromJson(json, SharedDynamicLink::class.java)
            }
        }
    }

    fun getGuestKey(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[GUEST_KEY]?.let { json ->
                gson.fromJson(json, String::class.java)
            }
        }
    }

    fun getVerifiedAccount(): Flow<SharedVerifiedAccount?> {
        return context.dataStore.data.map { prefs ->
            prefs[VERIFIED_ACCOUNT_KEY]?.let { json ->
                gson.fromJson(json, SharedVerifiedAccount::class.java)
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