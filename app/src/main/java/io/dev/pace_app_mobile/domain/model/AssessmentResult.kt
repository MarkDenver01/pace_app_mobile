package io.dev.pace_app_mobile.domain.model

data class CareerRequest(
    val careerName: String?  = ""
)

data class RecommendedCourseRequest(
    val courseDescription: String? = "",
    val assessmentResult: Double? = 0.0,
    val resultDescription: String? = "",
    val careers: List<CareerRequest>? = emptyList()
)

data class StudentAssessmentRequest(
    val email: String? = "",
    val userName: String? = "",
    val enrollmentStatus: String? = "",
    val enrolledUniversity: String? = "",
    val assessmentStatus: String? = "",
    val universityId: Long? = 0L,
    val recommendedCourseRequests: List<RecommendedCourseRequest>? = emptyList()
)

data class CareerResponse(
    val careerId: Long,
    val careerName: String,
    val courseId: Long
)

data class RecommendedCourseResponse(
    val courseId: Long,
    val courseDescription: String,
    val assessmentResult: Double,
    val assessmentDescription: String,
    val studentId: Long,
    val careers: List<CareerResponse>
)

data class StudentAssessmentResponse(
    val studentId: Long,
    val userName: String,
    val email: String,
    val enrollmentStatus: String,
    val enrolledUniversity: String,
    val assessmentStatus: String,
    val universityId: Long,
    val createdDateTime: String,
    val recommendedCourses: List<RecommendedCourseResponse>
)

