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
    PERSONAL("Personal Qualities");

    companion object {
        fun fromString(value: String): QuestionCategory {
            return when (value.uppercase()) {
                "GENERAL_INTEREST" -> GENERAL
                "CAREER_INTEREST" -> CAREER
                "PERSONAL_QUALITIES" -> PERSONAL
                else -> GENERAL
            }
        }
    }
}

data class QuestionResponse(
    val questionId: Int,
    val question: String,
    val category: String,
    val courseName: String,
    // val imgPath: String // TODO add img in backend
)