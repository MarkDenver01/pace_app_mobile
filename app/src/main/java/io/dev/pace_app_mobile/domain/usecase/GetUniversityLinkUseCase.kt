package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedUniversityLink
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUniversityLinkUseCase @Inject constructor(private val repo: LocalDataStoreRepository) {
    operator fun invoke(): Flow<SharedUniversityLink?> = repo.getUniversityLink()
}