package io.dev.pace_app_mobile.data.remote.repository

import io.dev.pace_app_mobile.data.local.datastore.DynamicLinkDataStore
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.repository.DynamicLinkRepository
import kotlinx.coroutines.flow.Flow

class DynamicLinkRepositoryImpl(
    private val dataStore: DynamicLinkDataStore
) : DynamicLinkRepository {

    override suspend fun saveDynamicLink(data: SharedDynamicLink) {
        dataStore.saveDynamicLink(data)
    }

    override fun getDynamicLink(): Flow<SharedDynamicLink?> {
        return dataStore.getDynamicLink()
    }

    override suspend fun updateVerification(isVerified: Boolean) {
        dataStore.updateVerification(isVerified)
    }

    override suspend fun clear() {
        dataStore.clear()
    }
}