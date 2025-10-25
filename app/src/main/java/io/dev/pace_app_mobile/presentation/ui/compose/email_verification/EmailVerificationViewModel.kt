package io.dev.pace_app_mobile.presentation.ui.compose.email_verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.usecase.DynamicLinkValidationUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityDomainEmailUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.VerificationCodeUseCase
import io.dev.pace_app_mobile.domain.usecase.VerifyAccountUseCase
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class EmailVerificationUiState(
    val isSending: Boolean = false,
    val countdown: Int = 0,
    val verifySuccess: Boolean = false,
    val errorMessage: String? = null,
    val isVerifyEnabled: Boolean = false
)

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val verifyAccountUseCase: VerifyAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailVerificationUiState())
    val uiState: StateFlow<EmailVerificationUiState> = _uiState

    private var countdownJob: Job? = null

    /**
     * Called when 4 digits are completed.
     * Starts countdown and enables "Verify Now" button.
     */
    fun onCodeComplete() {
        _uiState.update { it.copy(isVerifyEnabled = true) }
        startCountdown(50)
    }

    /**
     * Start countdown (resend timer)
     */
    private fun startCountdown(seconds: Int) {
        countdownJob?.cancel()
        _uiState.update { it.copy(countdown = seconds) }

        countdownJob = viewModelScope.launch {
            while (_uiState.value.countdown > 0) {
                delay(1000)
                _uiState.update { state -> state.copy(countdown = state.countdown - 1) }
            }

            // When timer reaches zero, disable verification
            _uiState.update { it.copy(isVerifyEnabled = false) }
        }
    }

    /**
     * Verify the 4-digit code
     */
    fun verifyCode(email: String, code: String) {
        if (code.length != 4) {
            _uiState.update { it.copy(errorMessage = "Code must be 4 digits") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(errorMessage = null, isSending = true) }
            try {
                delay(800)
                when (val result = verifyAccountUseCase.invoke(email, code.toInt())) {
                    is NetworkResult.Success -> {
                        if (result.data?.message == "success") {
                            countdownJob?.cancel()
                            _uiState.update { it.copy(verifySuccess = true, isSending = false) }
                        } else {
                            _uiState.update {
                                it.copy(
                                    errorMessage = "Invalid verification code",
                                    isSending = false
                                )
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = "Invalid verification code",
                                isSending = false
                            )
                        }
                    }

                    is NetworkResult.Loading -> {
                        // do nothing
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = ex.message ?: "Verification failed",
                        isSending = false
                    )
                }
            }
        }
    }

    /**
     * Resend verification code when timeout
     */
    fun resendCode(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, errorMessage = null) }
            delay(600)
            startCountdown(50)
            _uiState.update { it.copy(isSending = false) }
        }
    }

    fun reset() {
        countdownJob?.cancel()
        _uiState.value = EmailVerificationUiState()
    }
}
