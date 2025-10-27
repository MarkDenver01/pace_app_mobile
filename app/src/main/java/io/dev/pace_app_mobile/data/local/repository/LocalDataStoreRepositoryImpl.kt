package io.dev.pace_app_mobile.data.local.repository

import io.dev.pace_app_mobile.data.local.datastore.LocalDataStore
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedUniversityLink
import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import kotlinx.coroutines.flow.Flow

class LocalDataStoreRepositoryImpl(
    private val dataStore: LocalDataStore
) : LocalDataStoreRepository {

    override suspend fun saveDynamicLink(data: SharedDynamicLink) {
        dataStore.saveDynamicLink(data)
    }

    override fun getDynamicLink(): Flow<SharedDynamicLink?> {
        return dataStore.getDynamicLink()
    }

    override suspend fun saveUniversityLink(data: SharedUniversityLink) {
        dataStore.saveBaseUrl(data)
    }

    override fun getUniversityLink(): Flow<SharedUniversityLink?> {
        return dataStore.getUniversityLink()
    }

    override suspend fun updateVerification(isVerified: Boolean) {
        dataStore.updateVerification(isVerified)
    }

    override suspend fun saveVerifiedAccount(data: SharedVerifiedAccount) {
        dataStore.saveVerifiedAccount(data)
    }

    override fun getVerifiedAccount(): Flow<SharedVerifiedAccount?> {
        return dataStore.getVerifiedAccount()
    }

    override suspend fun clear() {
        dataStore.clear()
    }
}