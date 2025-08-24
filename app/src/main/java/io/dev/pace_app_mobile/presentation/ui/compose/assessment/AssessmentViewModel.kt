package io.dev.pace_app_mobile.presentation.ui.compose.assessment

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.model.AnsweredQuestionRequest
import io.dev.pace_app_mobile.domain.model.CourseRecommendation
import io.dev.pace_app_mobile.domain.model.Question
import io.dev.pace_app_mobile.domain.model.QuestionCategory
import io.dev.pace_app_mobile.domain.usecase.AllQuestionsByUniversityUseCase
import io.dev.pace_app_mobile.domain.usecase.CourseRecommendationUseCase
import io.dev.pace_app_mobile.domain.usecase.QuestionUseCase
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class AssessmentViewModel @Inject constructor(
    private val questionUseCase: QuestionUseCase,
    private val allQuestionsByUniversityUseCase: AllQuestionsByUniversityUseCase,
    private val recommendationUseCase: CourseRecommendationUseCase
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

    val totalQuestions: Int
        get() = _questions.value.size

    val currentQuestion: StateFlow<Question> =
        combine(_questions, _currentQuestionIndex) { questions, index ->
            questions.getOrNull(index) ?: Question(
                0,
                QuestionCategory.GENERAL,
                "Loading...",
                R.drawable.ic_placeholder,
                ""
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            Question(0, QuestionCategory.GENERAL, "Loading...", R.drawable.ic_placeholder, "")
        )

    val selectedAnswer: StateFlow<String?> =
        combine(_currentQuestionIndex, _answers, _questions) { index, answers, questions ->
            val questionId = questions.getOrNull(index)?.id
            questionId?.let { answers[it] }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        fetchAllQuestionsByUniversity()
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            val result = questionUseCase()
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
        }
    }

    private fun fetchAllQuestionsByUniversity() {
        viewModelScope.launch {
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
        }
    }

    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun onBeginClick() {
        _navigateTo.value = Routes.QUESTION_ROUTE // <- go to result later
    }

    fun onCompletedClick() {
        _navigateTo.value = Routes.QUESTION_COMPLETED_ROUTE
    }

    fun onViewResultsClick() {
        _navigateTo.value = Routes.COURSE_RECOMMENDATION_ROUTE
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

    fun getCurrentQuestion(): Question {
        return _questions.value.getOrNull(_currentQuestionIndex.value)
            ?: Question(0, QuestionCategory.GENERAL, "Loading...", R.drawable.ic_placeholder, "")
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
        fetchCourseRecommendation(buildAnsweredQuestions())
    }

    fun fetchCourseRecommendation(userAnswers: List<AnsweredQuestionRequest>) {
        viewModelScope.launch {
            val result = recommendationUseCase(userAnswers)
            result.fold(
                onSuccess = { recommendationList ->
                    _topCourses.value = recommendationList.map {
                        CourseRecommendation(
                            courseId = it.courseId,
                            courseName = it.courseName,
                            courseDescription = it.courseDescription,
                            matchPercentage = it.matchPercentage,
                            recommendationMessage = it.recommendationMessage
                        )
                    }
                },
                onFailure = {
                    // You may trigger a dialog or snackbar via UI
                    _questions.value = emptyList()
                }
            )
        }

        viewModelScope.launch {
            val result = questionUseCase()
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