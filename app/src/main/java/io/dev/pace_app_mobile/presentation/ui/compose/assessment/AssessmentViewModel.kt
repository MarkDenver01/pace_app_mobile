package io.dev.pace_app_mobile.presentation.ui.compose.assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.enums.HttpStatus
import io.dev.pace_app_mobile.domain.enums.UserType
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendation
import io.dev.pace_app_mobile.domain.model.LoginResponse
import io.dev.pace_app_mobile.domain.model.Question
import io.dev.pace_app_mobile.domain.model.QuestionCategory
import io.dev.pace_app_mobile.domain.model.StudentAssessmentRequest
import io.dev.pace_app_mobile.domain.model.StudentAssessmentResponse
import io.dev.pace_app_mobile.domain.usecase.AllQuestionsByUniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.CourseRecommendationUseCase
import io.dev.pace_app_mobile.domain.usecase.DeleteStudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.GetStudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.QuestionUseCase
import io.dev.pace_app_mobile.domain.usecase.StudentAssessmentUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateStudentPasswordUseCase
import io.dev.pace_app_mobile.domain.usecase.UpdateUserNameUseCase
import io.dev.pace_app_mobile.navigation.Routes
import io.dev.pace_app_mobile.presentation.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.collections.map

@HiltViewModel
class AssessmentViewModel @Inject constructor(
    private val questionUseCase: QuestionUseCase,
    private val allQuestionsByUniversityUseCase: AllQuestionsByUniversityUseCase,
    private val recommendationUseCase: CourseRecommendationUseCase,
    private val studentAssessmentUseCase: StudentAssessmentUseCase,
    private val getStudentAssessmentUseCase: GetStudentAssessmentUseCase,
    private val deleteStudentAssessmentUseCase: DeleteStudentAssessmentUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val updateStudentPasswordUseCase: UpdateStudentPasswordUseCase
) : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _answers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val answers = _answers.asStateFlow()

    private val _topCourses = MutableStateFlow<List<CourseRecommendation>>(emptyList())
    val topCourses = _topCourses.asStateFlow()

    val answeredQuestions = mutableListOf<AnsweredQuestionRequest>()

    private val _isLoadingQuestions = MutableStateFlow(false)
    val isLoadingQuestions: StateFlow<Boolean> = _isLoadingQuestions

    val totalQuestions: Int
        get() = _questions.value.size

    private val _showOldNewStudentDialog = MutableStateFlow(false)
    val showOldNewStudentDialog: StateFlow<Boolean> = _showOldNewStudentDialog

    private val _deleteStudentAssessment = MutableStateFlow(false)
    val deleteStudentAssessment: StateFlow<Boolean> = _deleteStudentAssessment
    private val _studentAssessmentRequest = MutableStateFlow<StudentAssessmentRequest?>(null)
    val studentAssessmentRequest = _studentAssessmentRequest.asStateFlow()

    private val _studentAssessmentResponse = MutableStateFlow<StudentAssessmentResponse?>(null)
    val studentAssessResponse: StateFlow<StudentAssessmentResponse?> = _studentAssessmentResponse

    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse = _loginResponse.asStateFlow()

    private val _university = MutableStateFlow<String?>(null)
    val university: StateFlow<String?> = _university

    private val _updateResult = MutableStateFlow<NetworkResult<Map<String, String>>?>(null)
    val updateResult: StateFlow<NetworkResult<Map<String, String>>?> = _updateResult
    private val _updatePasswordResult = MutableStateFlow<NetworkResult<Map<String, String>>?>(null)
    val updatePasswordResult: StateFlow<NetworkResult<Map<String, String>>?> = _updatePasswordResult


    fun setLoginResponse(loginResponse: LoginResponse?) {
        _loginResponse.value = loginResponse
    }

    fun setStudentAssessment(studentAssessmentRequest: StudentAssessmentRequest?) {
        _studentAssessmentRequest.value = studentAssessmentRequest
    }

    fun setExamResult(studentAssessmentRequest: StudentAssessmentRequest?) {
        _studentAssessmentRequest.value = studentAssessmentRequest
    }

    fun updateStudentUsername(userName: String) {
        _loginResponse.value = _loginResponse.value?.let { current ->
            current.copy(
                username = userName,
                studentResponse = current.studentResponse?.copy(
                    userName = userName
                )
            )
        }
    }

    fun updateStudentPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                _updatePasswordResult.value = NetworkResult.Loading()
                val result = updateStudentPasswordUseCase(email, newPassword)
                _updatePasswordResult.value = result
            }
        }
    }

    fun updateUserName(userName: String, email: String) {
        viewModelScope.launch {
            _updateResult.value = NetworkResult.Loading()
            val result = updateUserNameUseCase(userName, email)
            _updateResult.value = result
        }
    }

    fun getStudentAssessment(universityId: Long, email: String) {
        viewModelScope.launch {
            val result = getStudentAssessmentUseCase(
                universityId, email
            )

            when (result) {
                is NetworkResult.Success -> {
                    _studentAssessmentResponse.value = result.data
                }

                is NetworkResult.Error -> {
                    Timber.e("error logs: ${result.message}")
                }

                is NetworkResult.Loading -> {
                    // do nothing
                }
            }
        }
    }

    fun resetAssessmentStatus() {
        _studentAssessmentResponse.value = _studentAssessmentResponse.value?.copy(
            assessmentStatus = ""
        )
    }

    fun saveStudentAssessment(studentAssessmentRequest: StudentAssessmentRequest?) {
        viewModelScope.launch {
            val request = StudentAssessmentRequest(
                email = studentAssessmentRequest?.email,
                userName = studentAssessmentRequest?.userName,
                enrollmentStatus = studentAssessmentRequest?.enrollmentStatus,
                enrolledUniversity = studentAssessmentRequest?.enrolledUniversity,
                assessmentStatus = studentAssessmentRequest?.assessmentStatus,
                universityId = studentAssessmentRequest?.universityId,
                recommendedCourseRequests = studentAssessmentRequest?.recommendedCourseRequests
            )
            val result = studentAssessmentUseCase.invoke(request)
            when (result) {
                is NetworkResult.Success -> {
                    Timber.e("xxxx success")
                }

                is NetworkResult.Error -> {
                    Timber.e("xxxx error: ${result.message}")
                }

                is NetworkResult.Loading -> {
                    // do nothing
                }
            }
        }
    }

    val currentQuestion: StateFlow<Question> =
        combine(_questions, _currentQuestionIndex) { questions, index ->
            questions.getOrNull(index) ?: Question(
                0,
                QuestionCategory.CAREER,
                "Loading...",
                R.drawable.ic_placeholder,
                ""
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            Question(0, QuestionCategory.CAREER, "Loading...", R.drawable.ic_placeholder, "")
        )

    val selectedAnswer: StateFlow<String?> =
        combine(_currentQuestionIndex, _answers, _questions) { index, answers, questions ->
            val questionId = questions.getOrNull(index)?.id
            questionId?.let { answers[it] }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun fetchQuestions() {
        viewModelScope.launch {
            _isLoadingQuestions.value = true
            val result = questionUseCase()
            result.fold(
                onSuccess = { questionList ->
                    _questions.value = questionList.map {
                        Question(
                            id = it.questionId,
                            category = QuestionCategory.fromString(it.category),
                            text = it.question,
                            imageResId = R.drawable.ic_figure_1, // Update if category-based image mapping needed
                            courseName = it.courseName
                        )
                    }
                },
                onFailure = {
                    // You may trigger a dialog or snackbar via UI
                    _questions.value = emptyList()
                }
            )
            _isLoadingQuestions.value = false
        }
    }

    fun fetchAllQuestionsByUniversity() {
        viewModelScope.launch {
            _isLoadingQuestions.value = true
            val result = allQuestionsByUniversityUseCase()
            result.fold(
                onSuccess = { questionList ->
                    _questions.value = questionList.map {
                        Question(
                            id = it.questionId,
                            category = QuestionCategory.fromString(it.category),
                            text = it.question,
                            imageResId = R.drawable.ic_figure_1, // Update if category-based image mapping needed,
                            courseName = it.courseName
                        )
                    }
                },
                onFailure = {
                    // You may trigger a dialog or snackbar via UI
                    _questions.value = emptyList()
                }
            )
            _isLoadingQuestions.value = false
        }
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onBeginClick() {
        _navigateTo.value = Routes.QUESTION_ROUTE // <- go to result later
    }

    fun onViewRecommendedCourse() {
        _navigateTo.value = Routes.VIEW_RECOMMENDED_COURSE
    }

    fun onProfileClick() {
        _navigateTo.value = Routes.USER_PROFILE_ROUTE
    }

    fun onAboutClick() {
        _navigateTo.value = Routes.ABOUT_US_ROUTE
    }

    fun onDoneClick() {
        _navigateTo.value = Routes.FINISH_ASSESSMENT_ROUTE
    }

    fun onCourseRecommendedClick() {
        _navigateTo.value = Routes.COURSE_RECOMMENDATION_ROUTE
    }

    fun onDoneAssessmentClick() {
        _navigateTo.value = Routes.START_ASSESSMENT_ROUTE
    }


    fun onViewResultsClick(userType: UserType) {
        Timber.e("user type: $userType")
        when (userType) {
            UserType.GUEST -> {
                _showOldNewStudentDialog.value = true
            }

            UserType.DEFAULT,
            UserType.OLD,
            UserType.NEW -> {
                _navigateTo.value = Routes.COURSE_RECOMMENDATION_ROUTE
            }

            UserType.NEW_WITH_GUEST,
            UserType.OLD_WITH_GUEST -> {
                // no nothing
            }
        }
    }

    // Refactored functions to update dialog state
    fun confirmOldStudent() {
        _showOldNewStudentDialog.value = false
        _navigateTo.value = "${Routes.SIGN_UP_ROUTE}/true"
    }

    fun confirmNewStudent() {
        _showOldNewStudentDialog.value = false
        _navigateTo.value = "${Routes.SIGN_UP_ROUTE}/false"
    }

    fun onAnswerClick(answer: String) {
        val question = _questions.value[_currentQuestionIndex.value]
        _answers.update { current ->
            current.toMutableMap().apply {
                put(question.id, answer)
            }
        }
    }

    fun resetAssessment() {
        _currentQuestionIndex.value = 0
        _answers.value = emptyMap()
        _navigateTo.value = null
        fetchAllQuestionsByUniversity()
    }


    fun goToNextQuestion() {
        if (_currentQuestionIndex.value < totalQuestions - 1) {
            _currentQuestionIndex.value++
        } else {
            onBeginClick()
        }
    }

    fun goToPreviousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        } else {
            onBeginClick()
        }
    }

    fun getCurrentQuestion(): Question {
        return _questions.value.getOrNull(_currentQuestionIndex.value)
            ?: Question(0, QuestionCategory.CAREER, "Loading...", R.drawable.ic_placeholder, "")
    }

    fun getCurrentCategoryProgress(): Pair<Int, Int> {
        val currentQuestionId = currentQuestion.value.id
        val currentCategory = currentQuestion.value.category

        val questionsInCategory = _questions.value.filter { it.category == currentCategory }
        val indexInCategory = questionsInCategory.indexOfFirst { it.id == currentQuestionId }

        return Pair(indexInCategory + 1, questionsInCategory.size)
    }

    fun setAnswer(questionId: Int, answer: String) {
        _answers.value = _answers.value.toMutableMap().apply {
            put(questionId, answer)
        }
    }

    fun getAnswer(questionId: Int): String? {
        return _answers.value[questionId]
    }

    fun onCompletedAssessment() {
        _navigateTo.value = Routes.QUESTION_COMPLETED_ROUTE
    }

    fun deleteStudentAssessment(email: String) {
        viewModelScope.launch {
            val success = deleteStudentAssessmentUseCase.invoke(email)
            _deleteStudentAssessment.value = success
        }
    }

    fun resetDeleteState() {
        _deleteStudentAssessment.value = false
    }


    fun fetchCourseRecommendation() {
        viewModelScope.launch {
            val result = recommendationUseCase(buildAnsweredQuestions())
            result.fold(
                onSuccess = { recommendationList ->
                    _topCourses.value = recommendationList.map {
                        CourseRecommendation(
                            courseId = it.courseId,
                            courseName = it.courseName,
                            courseDescription = it.courseDescription,
                            matchPercentage = it.matchPercentage,
                            recommendationMessage = it.recommendationMessage,
                            possibleCareers = it.possibleCareers
                        )
                    }
                },
                onFailure = {
                    _topCourses.value = emptyList()
                }
            )
        }
    }


    private fun buildAnsweredQuestions(): List<AnsweredQuestionRequest> {
        return answers.value.map { (questionId, answer) ->
            AnsweredQuestionRequest(
                questionId = questionId.toLong(),
                answer = answer
            )
        }
    }

}