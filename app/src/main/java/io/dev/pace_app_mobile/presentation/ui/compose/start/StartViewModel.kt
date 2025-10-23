package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.usecase.CustomizationUseCase
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import io.dev.pace_app_mobile.presentation.utils.resolveLogoUrl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val customizationUseCase: CustomizationUseCase
) : ViewModel() {
    private val _navigateTo = MutableSharedFlow<Unit>()

    val navigateTo = _navigateTo.asSharedFlow()

    private val _logoPath = MutableStateFlow<String?>(null)
    val logoPath: StateFlow<String?> = _logoPath

    fun onStartClick() {
        viewModelScope.launch { _navigateTo.emit(Unit) }
    }

    fun fetchLogoPathWithDynamicUrl(universityId: Long) {
        viewModelScope.launch {
            when (val result = customizationUseCase(universityId)) {
                is NetworkResult.Success -> {
                    _logoPath.value = result.data?.logoUrl
                }
                is NetworkResult.Error -> {
                    // Optional fallback
                    _logoPath.value = resolveLogoUrl("")
                }
                is NetworkResult.Loading -> {
                    // You can show loading placeholder later
                }
            }
        }
    }
}