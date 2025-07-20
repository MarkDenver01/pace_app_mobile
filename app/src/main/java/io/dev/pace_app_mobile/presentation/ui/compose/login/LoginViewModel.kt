package io.dev.pace_app_mobile.presentation.ui.compose.login

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    // handle navigation route
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo.asStateFlow()

    // show dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    // remember login
    private val _isRemember = MutableStateFlow(false)
    val isRemember: StateFlow<Boolean> = _isRemember.asStateFlow()

    // handler error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    fun onNavigationHandled() {
        _navigateTo.value = null
    }

    fun onDialogDismissed() {
        _showDialog.value = false
        _navigateTo.value = Routes.START_ASSESSMENT_ROUTE
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }

    fun setRemember(value: Boolean) {
        _isRemember.value = value
    }

    fun onForgotPasswordClick() {}

    fun onLoginClick(email: String, password: String) {
        viewModelScope.launch {
            // get result from use case
            loginUseCase(email, password)
                .onSuccess {
                    // show dialog
                    _showDialog.value = true
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Unknown error occur"
                }
        }
    }

    fun onSignupClick() {
       _navigateTo.value = Routes.SIGN_UP_ROUTE
    }

    fun onAuthGoogleClick() {}

    fun onAuthFacebookClick() {}

    fun onAuthTwitterClick() {}

    fun onAuthInstagramClick() {}
}