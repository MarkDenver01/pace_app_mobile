package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import kotlinx.coroutines.flow.Flow

interface LocalDataStoreRepository {
    suspend fun saveDynamicLink(data: SharedDynamicLink)
    fun getDynamicLink(): Flow<SharedDynamicLink?>
    suspend fun updateVerification(isVerified: Boolean)

    suspend fun saveVerifiedAccount(data: SharedVerifiedAccount)

    fun getVerifiedAccount(): Flow<SharedVerifiedAccount?>
    suspend fun clear()
}