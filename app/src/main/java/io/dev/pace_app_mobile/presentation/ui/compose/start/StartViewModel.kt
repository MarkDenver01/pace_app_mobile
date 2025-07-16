package io.dev.pace_app_mobile.presentation.ui.compose.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {
    private val _navigateTo = MutableSharedFlow<Unit>()

    val navigateTo = _navigateTo.asSharedFlow()

    fun onStartClick() {
        viewModelScope.launch { _navigateTo.emit(Unit) }
    }
}