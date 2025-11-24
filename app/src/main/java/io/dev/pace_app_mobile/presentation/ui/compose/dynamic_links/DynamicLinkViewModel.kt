package io.dev.pace_app_mobile.presentation.ui.compose.dynamic_links

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.domain.model.SharedDynamicLink
import io.dev.pace_app_mobile.domain.model.SharedUniversityLink
import io.dev.pace_app_mobile.domain.usecase.GetDynamicLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.GetUniversityLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.GetVerifiedAccountUseCase
import io.dev.pace_app_mobile.domain.usecase.SaveDynamicLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.SaveUniversityIdUseCase
import io.dev.pace_app_mobile.domain.usecase.SaveUniversityLinkUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateVerificationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DynamicLinkViewModel @Inject constructor(
    private val saveDynamicLinkUseCase: SaveDynamicLinkUseCase,
    private val getDynamicLinkUseCase: GetDynamicLinkUseCase,
    private val updateVerificationUseCase: UpdateVerificationUseCase,
    private val getVerifiedAccountUseCase: GetVerifiedAccountUseCase,
    private val saveUniversityLinkUseCase: SaveUniversityLinkUseCase,
    private val getUniversityLinkUseCase: GetUniversityLinkUseCase,
    private val saveUniversityIdUseCase: SaveUniversityIdUseCase
) : ViewModel() {

    val dynamicLink = getDynamicLinkUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val verifiedAccount = getVerifiedAccountUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val universityLink = getUniversityLinkUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveUniversityIdViaDynamicLink(universityId: Long) {
        viewModelScope.launch {
            saveUniversityIdUseCase.invoke(universityId)
        }
    }

    fun saveLink(link: SharedDynamicLink) {
        viewModelScope.launch {
            saveDynamicLinkUseCase(link)
        }
    }

    fun saveUniversityLink(link: SharedUniversityLink) {
        viewModelScope.launch {
            saveUniversityLinkUseCase(link)
        }
    }

    fun verifyLink() {
        viewModelScope.launch {
            updateVerificationUseCase(true)
        }
    }
}