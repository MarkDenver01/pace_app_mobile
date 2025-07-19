package io.dev.pace_app_mobile.domain.model

data class Question(
    val id: Int,
    val category: QuestionCategory,
    val text: String,
    val imageResId: Int
)

enum class QuestionCategory(val displayName: String) {
    GENERAL("General Interest"),
    CAREER("Career Interest"),
    PERSONAL("Personal Qualities")
}