package io.dev.pace_app_mobile.presentation.ui.compose.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    private val _showAgreeWarningDialog = MutableStateFlow(false)
    val showAgreeWarningDialog: StateFlow<Boolean> = _showAgreeWarningDialog.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onSignupClick(agreed: Boolean, username: String, email: String, password: String, onSuccess: () -> Unit) {
        if (!agreed) {
            setShowAgreeWarningDialog(true)
            return
        }
        viewModelScope.launch {
            try {
                val result = registerUseCase(
                    username = username,
                    email = email,
                    password = password
                )

                if (result.isSuccess) {
                    onSuccess()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown error"
                    _showErrorDialog.value = true
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unexpected error occurred"
                _showErrorDialog.value = true
            }
        }
    }

    fun setShowAgreeWarningDialog(show: Boolean) {
        _showAgreeWarningDialog.value = show
    }

    fun showSuccessDialog() {
        _showSuccessDialog.value = true
    }

    fun dismissDialogs() {
        _showAgreeWarningDialog.value = false
        _showSuccessDialog.value = false
        _showErrorDialog.value = false
        _errorMessage.value = null
    }

    fun onSuccessTransition() {
        _navigateTo.value = Routes.LOGIN_ROUTE
    }
}