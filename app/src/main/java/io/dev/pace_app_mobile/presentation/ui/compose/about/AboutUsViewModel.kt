package io.dev.pace_app_mobile.presentation.ui.compose.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AboutContent(
    val vision: String = "",
    val mission: String = "",
    val coreValues: List<String> = emptyList()
)

class AboutUsViewModel : ViewModel() {
    private val _aboutContent = MutableStateFlow(AboutContent())
    val aboutContent = _aboutContent.asStateFlow()

    fun setAboutText(data: String) {
        viewModelScope.launch {
            val parsedData = parseAboutText(data)
            _aboutContent.value =_aboutContent.value.copy(
                vision = parsedData.vision,
                mission = parsedData.mission,
                coreValues = parsedData.coreValues)
        }
    }

    private fun parseAboutText(aboutText: String): AboutContent {
        val lines = aboutText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        val visionBuilder = StringBuilder()
        val missionBuilder = StringBuilder()
        val coreValues = mutableListOf<String>()

        var currentSection = ""

        for (line in lines) {
            when {
                line.contains("Vision", ignoreCase = true) -> currentSection = "vision"
                line.contains("Mission", ignoreCase = true) -> currentSection = "mission"
                line.contains("Core Values", ignoreCase = true) -> currentSection = "core"
                else -> {
                    when (currentSection) {
                        "vision" -> visionBuilder.appendLine(line)
                        "mission" -> missionBuilder.appendLine(line)
                        "core" -> coreValues.add(line)
                    }
                }
            }
        }

        return AboutContent(
            vision = visionBuilder.toString().trim(),
            mission = missionBuilder.toString().trim(),
            coreValues = coreValues
        )
    }
}


