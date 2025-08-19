package io.dev.pace_app_mobile.presentation.ui.compose.login

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.usecase.GoogleLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val universityUseCase: UniversityUseCase
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

    // selected university id
    private val _selectedUniversityId = MutableStateFlow<Long?>(null)
    val selectedUniversityId: StateFlow<Long?> = _selectedUniversityId

    // university
    private val _showUniversityDialog = MutableStateFlow(false)
    val showUniversityDialog: StateFlow<Boolean> = _showUniversityDialog

    private val _universities = MutableStateFlow<List<UniversityResponse>>(emptyList())
    val universities: StateFlow<List<UniversityResponse>> = _universities

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

    // A function to fetch universities, which should be called when needed
    public fun fetchUniversities() {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase().onSuccess { universityList ->
                _universities.value = universityList
            }.onFailure {
                _errorMessage.value = "Failed to load universities."
                //_showErrorDialog.value = true
            }
        }
    }

    fun onAuthGoogleClick(idToken: String) {
        val universityId = _selectedUniversityId.value

        if (universityId == null) {
            _errorMessage.value = "Please select a university"
            return
        }

        viewModelScope.launch {
            val result = googleLoginUseCase(idToken, universityId)
            result
                .onSuccess {
                    _showDialog.value = true
                }
                .onFailure {
                    _errorMessage.value = it.message
                }
        }
    }

    fun onAuthFacebookClick() {}

    fun onAuthTwitterClick() {}

    fun onAuthInstagramClick() {}

    fun showUniversitySelector() {
        _showUniversityDialog.value = true
    }

    fun setSelectedUniversity(id: Long) {
        _selectedUniversityId.value = id
    }

    fun confirmUniversitySelection() {
        _showUniversityDialog.value = false
    }
}