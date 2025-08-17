package io.dev.pace_app_mobile.domain.usecase

import io.dev.pace_app_mobile.domain.repository.ApiRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: ApiRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String,
        universityId: Long
    ): Result<String> {
        return repository.register(
            username,
            email,
            setOf("USER"),
            password,
            universityId
        )
    }
}