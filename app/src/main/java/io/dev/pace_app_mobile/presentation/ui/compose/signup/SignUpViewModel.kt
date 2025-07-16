package io.dev.pace_app_mobile.presentation.ui.compose.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)

    val navigateTo = _navigateTo.asStateFlow()

    private val _isAgree = MutableStateFlow(false)

    val isAgree: StateFlow<Boolean> = _isAgree.asStateFlow()


    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onSignUpClick() {
    }

    fun setAgree(value: Boolean) {
        _isAgree.value = value
    }
}