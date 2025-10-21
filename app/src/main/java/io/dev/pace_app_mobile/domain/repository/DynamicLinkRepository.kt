package io.dev.pace_app_mobile.domain.repository

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import kotlinx.coroutines.flow.Flow

interface DynamicLinkRepository {
    suspend fun saveDynamicLink(data: SharedDynamicLink)
    fun getDynamicLink(): Flow<SharedDynamicLink?>
    suspend fun updateVerification(isVerified: Boolean)
    suspend fun clear()
}