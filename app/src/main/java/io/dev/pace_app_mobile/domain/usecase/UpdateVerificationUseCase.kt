package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.DynamicLinkRepository
import javax.inject.Inject

class UpdateVerificationUseCase @Inject constructor(private val repo: DynamicLinkRepository) {
    suspend operator fun invoke(isVerified: Boolean) = repo.updateVerification(isVerified)
}