package io.dev.pace_app_mobile.presentation.ui.compose.assessment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AssessmentViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)

    val navigateTo = _navigateTo.asStateFlow()

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onBeginClick() {
        _navigateTo.value = Routes.QUESTION_ROUTE
    }

    fun onAnswerClick() {}
}