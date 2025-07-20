package io.dev.pace_app_mobile.presentation.ui.compose.title

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)

    val navigateTo = _navigateTo.asStateFlow()

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onLoginClick() {
        _navigateTo.value = Routes.LOGIN_ROUTE
    }

    fun onSignUpClick() {
        _navigateTo.value = Routes.SIGN_UP_ROUTE
    }

}