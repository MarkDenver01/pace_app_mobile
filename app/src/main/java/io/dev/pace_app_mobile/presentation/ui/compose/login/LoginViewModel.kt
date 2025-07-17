package io.dev.pace_app_mobile.presentation.ui.compose.login

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)

    val navigateTo = _navigateTo.asStateFlow()

    private val _isRemember = MutableStateFlow(false)

    val isRemember: StateFlow<Boolean> = _isRemember.asStateFlow()

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun setRemember(value: Boolean) {
        _isRemember.value = value
    }

    fun onForgotPasswordClick() {}

    fun onLoginClick() {
        _navigateTo.value = Routes.START_ASSESSMENT_ROUTE
    }

    fun onSignupClick() {
        _navigateTo.value = Routes.SIGN_UP_ROUTE
    }

    fun onAuthGoogleClick() {}

    fun onAuthFacebookClick() {}

    fun onAuthTwitterClick() {}

    fun onAuthInstagramClick() {}
}