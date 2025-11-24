package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.model.QuestionResponse
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.domain.repository.LocalDataStoreRepository
import javax.inject.Inject

class SaveUniversityIdUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    suspend operator fun invoke(universityId: Long) {
        apiRepository.saveUniversityIdViaDynamicLink(universityId)
    }
}