package io.dev.pace_app_mobile.presentation.ui.compose.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.usecase.GoogleAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state for the main login screen
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
}

// One-time events for dialogs and navigation
sealed class LoginEvent {
    data class ShowSuccessDialog(val message: String) : LoginEvent()
    data class ShowErrorDialog(val message: String) : LoginEvent()
    data class ShowProgressDialog(val isVisible: Boolean) : LoginEvent()
    data class NavigateTo(val route: String) : LoginEvent()
    data class ShowUniversityDialog(val universities: List<UniversityResponse>, val googleIdToken: String) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val googleAccountUseCase: GoogleAccountUseCase,
    private val universityUseCase: UniversityUseCase
) : ViewModel() {

    // A persistent StateFlow for the main UI state
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    // A SharedFlow for one-time events (e.g., show a dialog or navigate)
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow: SharedFlow<LoginEvent> = _eventFlow

    private val _selectedUniversityId = MutableStateFlow<Long?>(null)
    val selectedUniversityId: StateFlow<Long?> = _selectedUniversityId.asStateFlow()

    fun onLoginClick(email: String, password: String) {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
            _uiState.value = LoginUiState.Loading

            val result = loginUseCase(email, password)

            _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            _uiState.value = LoginUiState.Idle

            when (result) {
                is NetworkResult.Success -> {
                    _eventFlow.emit(LoginEvent.ShowSuccessDialog("Login successful!"))
                }

                is NetworkResult.Error -> {
                    _eventFlow.emit(LoginEvent.ShowErrorDialog(result.message!!))
                }
                else -> {
                    // This state should not be handled here
                }
            }
        }
    }

    fun onSignupClick() {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.NavigateTo(Routes.SIGN_UP_ROUTE))
        }
    }

    fun onAuthGoogleClick(idToken: String) {
        val universityId = _selectedUniversityId.value
        if (universityId == null) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Please select a university."))
            }
            return
        }

        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.ShowProgressDialog(true))

            val result = googleLoginUseCase(idToken, universityId)
            _eventFlow.emit(LoginEvent.ShowProgressDialog(false))

            result
                .onSuccess { loginResult ->
                    when (loginResult.statusCode) {
                        201 -> {
                            _eventFlow.emit(LoginEvent.ShowErrorDialog("Google account exists, but no user found."))
                        }

                        200 -> {
                            _eventFlow.emit(LoginEvent.NavigateTo(Routes.START_ASSESSMENT_ROUTE))
                        }

                        else -> {
                            _eventFlow.emit(LoginEvent.ShowErrorDialog("Unexpected success status: ${loginResult.statusCode}"))
                        }
                    }
                }
                .onFailure {
                    _eventFlow.emit(LoginEvent.ShowErrorDialog(it.message ?: "Google login failed."))
                }
        }
    }

    fun onAuthFacebookClick() {}

    fun onAuthTwitterClick() {}

    fun onAuthInstagramClick() {}

    fun checkGoogleAccount(email: String, idToken: String) {
        viewModelScope.launch {
            try {
                _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
                val exists = googleAccountUseCase(email)
                _eventFlow.emit(LoginEvent.ShowProgressDialog(false))

                if (exists) {
                    onAuthGoogleClick(idToken)
                } else {
                    fetchUniversities(idToken)
                }
            } catch (e: Exception) {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Error checking Google account."))
                _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            }
        }
    }

    fun fetchUniversities(googleIdToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase().onSuccess { universityList ->
                _eventFlow.emit(LoginEvent.ShowUniversityDialog(universityList, googleIdToken))
            }.onFailure {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Failed to load universities."))
            }
        }
    }

    fun setSelectedUniversity(id: Long) {
        _selectedUniversityId.value = id
    }
}