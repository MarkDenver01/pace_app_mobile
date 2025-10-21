package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.DynamicLinkRepository

class UpdateVerificationUseCase(private val repo: DynamicLinkRepository) {
    suspend operator fun invoke(isVerified: Boolean) = repo.updateVerification(isVerified)
}