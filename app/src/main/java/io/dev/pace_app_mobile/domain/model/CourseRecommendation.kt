package io.dev.pace_app_mobile.domain.model



data class AnsweredQuestionRequest(
    val questionId: Long,
    val answer:String
)
data class CourseRecommendationResponse(
    val courseId: Long,
    val courseName: String,
    val courseDescription: String,
    val matchPercentage: Double,
    val recommendationMessage: String
)

data class CourseRecommendation (
    val courseId: Long,
    val courseName: String,
    val courseDescription: String,
    val matchPercentage: Double,
    val recommendationMessage: String
)