package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import kotlinx.coroutines.flow.Flow

interface LocalDataStoreRepository {
    suspend fun saveDynamicLink(data: SharedDynamicLink)

    suspend fun saveGuestKey(message: String)
    fun getDynamicLink(): Flow<SharedDynamicLink?>

    fun getGuestKey(): Flow<String?>
    suspend fun updateVerification(isVerified: Boolean)

    suspend fun saveVerifiedAccount(data: SharedVerifiedAccount)

    fun getVerifiedAccount(): Flow<SharedVerifiedAccount?>
    suspend fun clear()
}