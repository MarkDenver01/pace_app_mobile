package io.dev.pace_app_mobile.data.local.repository

import io.dev.pace_app_mobile.data.local.datastore.LocalDataStore
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedVerifiedAccount
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import kotlinx.coroutines.flow.Flow

class LocalDataStoreRepositoryImpl(
    private val dataStore: LocalDataStore
) : LocalDataStoreRepository {

    override suspend fun saveDynamicLink(data: SharedDynamicLink) {
        dataStore.saveDynamicLink(data)
    }

    override suspend fun saveGuestKey(message: String) {
       dataStore.saveGuestKey(message)
    }

    override fun getDynamicLink(): Flow<SharedDynamicLink?> {
        return dataStore.getDynamicLink()
    }

    override fun getGuestKey(): Flow<String?> {
        return dataStore.getGuestKey()
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