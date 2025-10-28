package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.ApiRepository
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import jakarta.inject.Inject

class UpdateStudentPasswordUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<Map<String, String>> =
        repository.updateStudentPassword(email, password)
}