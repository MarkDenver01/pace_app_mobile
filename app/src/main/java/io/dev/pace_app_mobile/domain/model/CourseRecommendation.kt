package io.dev.pace_app_mobile.domain.model


/**
 * Answered question request
 *
 * @property questionId
 * @property answer
 * @constructor Create empty Answered question request
 */
data class AnsweredQuestionRequest(
    val questionId: Long,
    val answer:String
)

/**
 * Course recommendation response
 *
 * @property courseId
 * @property courseName
 * @property courseDescription
 * @property matchPercentage
 * @property recommendationMessage
 * @constructor Create empty Course recommendation response
 */
data class CourseRecommendationResponse(
    val courseId: Long,
    val courseName: String,
    val courseDescription: String,
    val matchPercentage: Double,
    val recommendationMessage: String
)

/**
 * Course recommendation
 *
 * @property courseId
 * @property courseName
 * @property courseDescription
 * @property matchPercentage
 * @property recommendationMessage
 * @constructor Create empty Course recommendation
 */
data class CourseRecommendation (
    val courseId: Long,
    val courseName: String,
    val courseDescription: String,
    val matchPercentage: Double,
    val recommendationMessage: String
)