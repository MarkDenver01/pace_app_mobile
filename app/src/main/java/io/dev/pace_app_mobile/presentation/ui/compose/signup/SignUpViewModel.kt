package io.dev.pace_app_mobile.presentation.ui.compose.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.model.UniversityDomainResponse
import io.dev.pace_app_mobile.domain.model.UniversityResponse
import io.dev.pace_app_mobile.domain.usecase.DynamicLinkValidationUseCase
import io.dev.pace_app_mobile.domain.usecase.RegisterUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityDomainEmailUseCase
import io.dev.pace_app_mobile.domain.usecase.UniversityUseCase
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val universityUseCase: UniversityUseCase,
    private val registerUseCase: RegisterUseCase,
    private val dynamicLinkValidationUseCase: DynamicLinkValidationUseCase,
    private val universityDomainEmailUseCase: UniversityDomainEmailUseCase
) : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    private val _universities = MutableStateFlow<List<UniversityResponse>>(emptyList())
    val universities: StateFlow<List<UniversityResponse>> = _universities.asStateFlow()

    private val _universityDomainEmail = MutableStateFlow<UniversityDomainResponse?>(null)
    val universityDomainEmail: StateFlow<UniversityDomainResponse?> = _universityDomainEmail.asStateFlow()

    private val _university = MutableStateFlow<UniversityResponse?>(null)
    val university: StateFlow<UniversityResponse?> = _university.asStateFlow()

    private val _showAgreeWarningDialog = MutableStateFlow(false)
    val showAgreeWarningDialog: StateFlow<Boolean> = _showAgreeWarningDialog.asStateFlow()

    private val _showInvalidTokenWarningDialog = MutableStateFlow(false)
    val showInvalidTokenWarningDialog: StateFlow<Boolean> =
        _showInvalidTokenWarningDialog.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun fetchUniversityById(universityId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            universityUseCase.invoke(universityId)
                .onSuccess { response ->
                    _university.value = response
                }
                .onFailure { e ->
                    _errorMessage.value = "Failed to get the university"
                    _showErrorDialog.value = true
                }
        }
    }

    fun fetchUniversityDomainEmail(universityId: Long) {
        viewModelScope.launch {
            when (val result = universityDomainEmailUseCase(universityId)) {
                is NetworkResult.Success -> {
                    _universityDomainEmail.value = result.data
                }
                is NetworkResult.Error -> {
                    Timber.e("Cannot find the domain email: ${result.message}")
                }
                is NetworkResult.Loading -> {
                    Timber.d("do nothing...")
                }
            }
        }
    }


    // You can also add a function to get the university ID from the name
    fun getUniversityId(universityName: String): Long? {
        return universities.value.find { it.universityName == universityName }?.universityId
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onSignupClick(
        agreed: Boolean,
        username: String,
        email: String,
        password: String,
        universityId: Long,
        token: String,
        onSuccess: () -> Unit
    ) {
        if (!agreed) {
            setShowAgreeWarningDialog(true)
            return
        }

        viewModelScope.launch {
            try {
                val result = dynamicLinkValidationUseCase(token)
                Timber.d("result: ${result.data?.message}")
                when (result) {
                    is NetworkResult.Success -> {
                        if (result.data?.message == "success") {
                            val registerResult = registerUseCase(
                                username = username,
                                email = email,
                                password = password,
                                universityId = universityId
                            )

                            if (registerResult.isSuccess) {
                                onSuccess()
                            } else {
                                _errorMessage.value = "Registration failed"
                                _showErrorDialog.value = true
                            }
                        } else {
                            setShowInvalidTokenWarningDialog(true)
                        }

                    }

                    is NetworkResult.Error -> {
                        setShowInvalidTokenWarningDialog(true)
                    }

                    else -> {
                        _errorMessage.value = "Unknown error"
                        _showErrorDialog.value = true
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error happens $e"
                _showErrorDialog.value = true
            }
        }
    }

    fun setShowAgreeWarningDialog(show: Boolean) {
        _showAgreeWarningDialog.value = show
    }

    fun setShowInvalidTokenWarningDialog(show: Boolean) {
        _showInvalidTokenWarningDialog.value = show
    }

    fun showSuccessDialog() {
        _showSuccessDialog.value = true
    }

    fun showErrorDialog(errorMessage: String) {
        _errorMessage.value = errorMessage
        _showErrorDialog.value = true
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