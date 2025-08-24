package io.dev.pace_app_mobile.domain.model


/**
 * Question
 *
 * @property id
 * @property category
 * @property text
 * @property imageResId
 * @property courseName
 * @constructor Create empty Question
 */
data class Question(
    val id: Int,
    val category: QuestionCategory,
    val text: String,
    val imageResId: Int,
    val courseName: String
)

/**
 * Question category
 *
 * @property displayName
 * @constructor Create empty Question category
 */
enum class QuestionCategory(val displayName: String) {
    /**
     * General
     *
     * @constructor Create empty General
     */
    GENERAL("General Interest"),

    /**
     * Career
     *
     * @constructor Create empty Career
     */
    CAREER("Career Interest"),

    /**
     * Personal
     *
     * @constructor Create empty Personal
     */
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

/**
 * Question response
 *
 * @property questionId
 * @property question
 * @property category
 * @property courseName
 * @constructor Create empty Question response
 */
data class QuestionResponse(
    val questionId: Int,
    val question: String,
    val category: String,
    val courseName: String,
    // val imgPath: String // TODO add img in backend
    val courseDescription: String,
    val universityId: Long,
    val universityName: String
)