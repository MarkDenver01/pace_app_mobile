package io.dev.pace_app_mobile.presentation.ui.compose.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dev.pace_app_mobile.data.local.prefs.TokenManager
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)

    val navigateTo = _navigateTo.asStateFlow()

    private val _isRemember = MutableStateFlow(false)

    val isRemember: StateFlow<Boolean> = _isRemember.asStateFlow()

    var loginState by mutableStateOf<Result<LoginResponse>?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginState = null
            val result = loginUseCase(email, password)
            loginState = result
            result.onSuccess {
                _navigateTo.value = Routes.START_ASSESSMENT_ROUTE
            }
        }
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun setRemember(value: Boolean) {
        _isRemember.value = value
    }

    fun onForgotPasswordClick() {}

    fun onLoginClick(email: String, password: String) {
        login(email, password)
    }

    fun onSignupClick() {
        _navigateTo.value = Routes.SIGN_UP_ROUTE
    }

    fun onAuthGoogleClick() {}

    fun onAuthFacebookClick() {}

    fun onAuthTwitterClick() {}

    fun onAuthInstagramClick() {}
}