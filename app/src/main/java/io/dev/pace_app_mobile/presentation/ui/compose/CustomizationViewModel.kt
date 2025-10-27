package io.dev.pace_app_mobile.presentation.ui.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.enums.Customization
import io.dev.pace_app_mobile.domain.usecase.CustomizationUseCase
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomizationViewModel @Inject constructor(
    private val customizationUseCase: CustomizationUseCase,
) : ViewModel() {

    private val _themeState = MutableStateFlow<Customization?>(null)
    val themeState: StateFlow<Customization?> = _themeState.asStateFlow()

    fun loadTheme(universityId: Long) {
        viewModelScope.launch {
            when (val result = customizationUseCase(universityId)) {
                is NetworkResult.Success -> {
                    val themeName = result.data?.themeName ?: "light"
                    _themeState.value = mapTheme(themeName)
                }
                is NetworkResult.Error -> {
                    // fallback if error
                    _themeState.value = Customization.lightTheme
                }
                is NetworkResult.Loading -> {
                    Timber.d("do nothing...")
                }
            }
        }
    }

    private fun mapTheme(themeName: String): Customization {
        return when (themeName.lowercase()) {
            "dark" -> Customization.darkTheme
            "redish" -> Customization.redishTheme
            "purplelish" -> Customization.purplelishTheme
            "brownish" -> Customization.brownishTheme
            "greenish" -> Customization.greenishTheme
            "blueish" -> Customization.blueishTheme
            "maroonish" -> Customization.maroonishTheme
            "light" -> Customization.lightTheme
            else -> Customization.lightTheme
        }
    }

}