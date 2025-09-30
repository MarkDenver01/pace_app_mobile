package io.dev.pace_app_mobile.presentation.ui.compose.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.enums.HttpStatus
import io.dev.pace_app_mobile.domain.model.LoginResult
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.usecase.FacebookAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.FacebookLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.GoogleLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.InstagramLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.LoginUseCase
import io.dev.pace_app_mobile.domain.usecase.TwitterLoginUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import io.dev.pace_app_mobile.presentation.utils.getHttpStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
    data class ShowWarningDialog(val message: String) : LoginEvent()
    data class ShowProgressDialog(val isVisible: Boolean) : LoginEvent()
    data class NavigateTo(val route: String) : LoginEvent()

    // Google new-user flow
    data class ShowUniversityDialog(
        val universities: List<UniversityResponse>,
        val googleIdToken: String
    ) : LoginEvent()

    // Facebook new-user flow
    data class ShowUniversityDialogFacebook(
        val universities: List<UniversityResponse>,
        val facebookAccessToken: String
    ) : LoginEvent()

    data class ShowUniversityDialogTwitter(
        val universities: List<UniversityResponse>,
        val twitterAccessToken: String
    ) : LoginEvent()

    data class ShowUniversityDialogInstagram(
        val universities: List<UniversityResponse>,
        val authorizationCode: String
    ) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val facebookLoginUseCase: FacebookLoginUseCase,
    private val twitterLoginUseCase: TwitterLoginUseCase,
    private val instagramLoginUseCase: InstagramLoginUseCase,
    private val googleAccountUseCase: GoogleAccountUseCase,
    private val facebookAccountUseCase: FacebookAccountUseCase,
    private val universityUseCase: UniversityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

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
                is NetworkResult.Success ->
                    _eventFlow.emit(LoginEvent.ShowSuccessDialog("Login successful!"))

                is NetworkResult.Error ->
                    _eventFlow.emit(LoginEvent.ShowErrorDialog(result.message ?: "Login failed."))

                else -> Unit
            }
        }
    }

    fun onSignupClick() {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.NavigateTo(Routes.SIGN_UP_ROUTE))
        }
    }

    fun onAuthGoogleClick(idToken: String, exists: Boolean) {
        if (exists) {
            loginWithGoogle(idToken, null)
        } else {
            val universityId = _selectedUniversityId.value
            if (universityId != null) {
                loginWithGoogle(idToken, universityId)
            } else {
                viewModelScope.launch {
                    _eventFlow.emit(LoginEvent.ShowErrorDialog("Please select a university"))
                }
            }
        }
    }

    fun onAuthFacebookClick(accessToken: String, exists: Boolean) {
        if (exists) {
            loginWithFacebook(accessToken, null)
        } else {
            val universityId = _selectedUniversityId.value
            if (universityId != null) {
                loginWithFacebook(accessToken, universityId)
            } else {
                viewModelScope.launch {
                    _eventFlow.emit(LoginEvent.ShowErrorDialog("Please select a university"))
                }
            }
        }
    }

    private fun loginWithGoogle(idToken: String, universityId: Long?) {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
            val result = googleLoginUseCase(idToken, universityId)
            _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            handleGoogleLoginResult(result)
        }
    }

    private fun loginWithFacebook(accessToken: String, universityId: Long?) {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
            val result = facebookLoginUseCase(accessToken, universityId)
            _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            handleFacebookLoginResult(result, accessToken)
        }
    }

    private suspend fun handleFacebookLoginResult(
        result: NetworkResult<LoginResult>,
        accessToken: String
    ) {
        when (result) {
            is NetworkResult.Success -> {
                val loginResult = result.data
                when (getHttpStatus(loginResult?.statusCode ?: 0)) {
                    HttpStatus.OK -> {
                        val status = loginResult?.loginResponse?.studentResponse?.userAccountStatus
                        if (status.equals("PENDING")) {
                            _eventFlow.emit(
                                LoginEvent.ShowWarningDialog(
                                    "Please wait to approved your account by the admin. Thank you."
                                )
                            )
                        } else {
                            _eventFlow.emit(LoginEvent.ShowSuccessDialog("Login successful"))
                        }
                    }

                    HttpStatus.CREATED -> {
                        _eventFlow.emit(
                            LoginEvent.ShowWarningDialog(
                                "Please wait to approved your account by the admin. Thank you."
                            )
                        )
                    }

                    // New Facebook user → backend requires university → open picker
                    HttpStatus.BAD_REQUEST -> {
                        fetchUniversitiesForFacebook(accessToken)
                    }

                    else -> _eventFlow.emit(
                        LoginEvent.ShowErrorDialog("Unexpected status: ${loginResult?.statusCode}")
                    )
                }
            }

            is NetworkResult.Error -> {
                _eventFlow.emit(
                    LoginEvent.ShowErrorDialog(
                        result.message ?: "Facebook login failed."
                    )
                )
            }

            else -> Unit
        }
    }

    private suspend fun handleGoogleLoginResult(result: NetworkResult<LoginResult>) {
        when (result) {
            is NetworkResult.Success -> {
                val loginResult = result.data
                when (getHttpStatus(loginResult?.statusCode ?: 0)) {
                    HttpStatus.OK -> {
                        val status = loginResult?.loginResponse?.studentResponse?.userAccountStatus
                        if (status.equals("PENDING")) {
                            _eventFlow.emit(
                                LoginEvent.ShowWarningDialog(
                                    "Please wait to approved your account by the admin. Thank you."
                                )
                            )
                        } else {
                            _eventFlow.emit(LoginEvent.ShowSuccessDialog("Login successful"))
                        }
                    }

                    HttpStatus.CREATED -> _eventFlow.emit(
                        LoginEvent.ShowWarningDialog(
                            "Please wait to approved your account by the admin. Thank you."
                        )
                    )

                    HttpStatus.BAD_REQUEST -> _eventFlow.emit(
                        LoginEvent.ShowErrorDialog("University is required for new users.")
                    )

                    else -> _eventFlow.emit(
                        LoginEvent.ShowErrorDialog("Unexpected status: ${loginResult?.statusCode}")
                    )
                }
            }

            is NetworkResult.Error -> {
                _eventFlow.emit(
                    LoginEvent.ShowErrorDialog(result.message ?: "Google login failed.")
                )
            }

            else -> Unit
        }
    }

    fun onAuthTwitterClick(accessToken: String, exists: Boolean) {
        if (exists) {
            loginWithTwitter(accessToken, null)
        } else {
            val uniId = _selectedUniversityId.value
            if (uniId != null) loginWithTwitter(accessToken, uniId) else viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Please select a university"))
            }
        }
    }
    fun onAuthInstagramClick(authorizationCode: String, exists: Boolean) {
        if (exists) {
            loginWithInstagramCode(authorizationCode, null)
        } else {
            val uniId = _selectedUniversityId.value
            if (uniId != null) loginWithInstagramCode(authorizationCode, uniId) else viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Please select a university"))
            }
        }
    }

    private fun loginWithTwitter(accessToken: String, universityId: Long?) {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
            val result = twitterLoginUseCase(accessToken, universityId)
            _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            handleCommonSocialResult(result) // reuse same handler as Google/Facebook if you extracted it
        }
    }

    private fun loginWithInstagramCode(code: String, universityId: Long?) {
        viewModelScope.launch {
            _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
            val result = instagramLoginUseCase(code, universityId)
            _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            handleCommonSocialResult(result)
        }
    }

    private suspend fun handleCommonSocialResult(result: NetworkResult<LoginResult>) {
        when (result) {
            is NetworkResult.Success -> {
                val loginResult = result.data
                when (getHttpStatus(loginResult?.statusCode ?: 0)) {
                    HttpStatus.OK -> emitStatus(loginResult, success = true)
                    HttpStatus.CREATED -> _eventFlow.emit(LoginEvent.ShowWarningDialog("Please wait to approved your account by the admin. Thank you."))
                    HttpStatus.BAD_REQUEST -> {
                        // caller should already have issued a fetchUniversities*() before calling login for new users
                        _eventFlow.emit(LoginEvent.ShowErrorDialog("University is required for new users."))
                    }
                    else -> _eventFlow.emit(LoginEvent.ShowErrorDialog("Unexpected status: ${loginResult?.statusCode}"))
                }
            }
            is NetworkResult.Error -> _eventFlow.emit(LoginEvent.ShowErrorDialog(result.message ?: "Login failed."))
            else -> Unit
        }
    }

    private suspend fun emitStatus(loginResult: LoginResult?, success: Boolean) {
        val status = loginResult?.loginResponse?.studentResponse?.userAccountStatus
        if (status == "PENDING") {
            _eventFlow.emit(LoginEvent.ShowWarningDialog("Please wait to approved your account by the admin. Thank you."))
        } else if (success) {
            _eventFlow.emit(LoginEvent.ShowSuccessDialog("Login successful"))
        }
    }

    fun checkGoogleAccount(email: String, idToken: String) {
        viewModelScope.launch {
            try {
                _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
                val exists = googleAccountUseCase(email)
                _eventFlow.emit(LoginEvent.ShowProgressDialog(false))

                if (exists) {
                    onAuthGoogleClick(idToken, true)
                } else {
                    fetchUniversitiesForGoogle(idToken)
                }
            } catch (e: Exception) {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Error checking Google account."))
                _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            }
        }
    }

    fun checkFacebookAccount(accessToken: String) {
        viewModelScope.launch {
            try {
                _eventFlow.emit(LoginEvent.ShowProgressDialog(true))
                val exists = facebookAccountUseCase(accessToken)
                _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
                Timber.d("checking facebook account: $exists")
                if (exists) {
                    onAuthFacebookClick(accessToken, true)
                } else {
                    fetchUniversitiesForFacebook(accessToken)
                }
            } catch (e: Exception) {
                Timber.e("Facebook error: ${e.message}")
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Error checking Facebook account."))
                _eventFlow.emit(LoginEvent.ShowProgressDialog(false))
            }
        }
    }

    private fun fetchUniversitiesForGoogle(googleIdToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase().onSuccess { universityList ->
                _eventFlow.emit(LoginEvent.ShowUniversityDialog(universityList, googleIdToken))
            }.onFailure {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Failed to load universities."))
            }
        }
    }

    private fun fetchUniversitiesForFacebook(facebookAccessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase().onSuccess { universityList ->
                _eventFlow.emit(
                    LoginEvent.ShowUniversityDialogFacebook(
                        universityList,
                        facebookAccessToken
                    )
                )
            }.onFailure {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Failed to load universities."))
            }
        }
    }

    fun setSelectedUniversity(id: Long) {
        _selectedUniversityId.value = id
    }

    // Helpers to preload universities for new-user flows (like existing Google/Facebook)
    fun startTwitterNewUser(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase().onSuccess { list ->
                _eventFlow.emit(LoginEvent.ShowUniversityDialogTwitter(list, accessToken))
            }.onFailure { _eventFlow.emit(LoginEvent.ShowErrorDialog("Failed to load universities.")) }
        }
    }


    fun startInstagramNewUser(authCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase().onSuccess { list ->
                _eventFlow.emit(LoginEvent.ShowUniversityDialogInstagram(list, authCode))
            }.onFailure { _eventFlow.emit(LoginEvent.ShowErrorDialog("Failed to load universities.")) }
        }
    }

    fun handleTwitterRedirect(code: String?, error: String?) {
        if (error != null) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Twitter login failed: $error"))
            }
            return
        }
        code?.let {
            // Check if this is a new user flow (needs university)
            startTwitterNewUser(it)
            // Or, if backend can handle immediate login:
            // onAuthTwitterClick(it, exists = true)
        }
    }

    fun handleInstagramRedirect(code: String?, error: String?) {
        if (error != null) {
            viewModelScope.launch {
                _eventFlow.emit(LoginEvent.ShowErrorDialog("Instagram login failed: $error"))
            }
            return
        }
        code?.let {
            startInstagramNewUser(it)
            // Or, if backend can handle immediate login:
            // onAuthInstagramClick(it, exists = true)
        }
    }

    fun isUserLoggedIn() : Boolean {
        return false // TODO
    }

}
