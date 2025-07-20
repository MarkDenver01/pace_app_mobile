package io.dev.pace_app_mobile.presentation.ui.compose.assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dev.pace_app_mobile.R
import io.dev.pace_app_mobile.domain.model.Question
import io.dev.pace_app_mobile.domain.model.QuestionCategory
import io.dev.pace_app_mobile.domain.usecase.QuestionUseCase
import io.dev.pace_app_mobile.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class AssessmentViewModel @Inject constructor(
    private val questionUseCase: QuestionUseCase
) : ViewModel() {
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex = _currentQuestionIndex.asStateFlow()

    private val _answers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val answers = _answers.asStateFlow()

//    private val allQuestions = listOf(
//        // General Interest
//        Question(1, QuestionCategory.GENERAL, "Are you interested in how businesses operate?", R.drawable.ic_figure_1),
//        Question(2, QuestionCategory.GENERAL, "Do you enjoy watching business-related news or documentaries?", R.drawable.ic_figure_1),
//        Question(3, QuestionCategory.GENERAL, "Are you curious about the stock market and investments?", R.drawable.ic_figure_1),
//
//        // Career Interest
//        Question(4, QuestionCategory.CAREER, "Are you interested in a career in finance?", R.drawable.ic_figure_2),
//        Question(5, QuestionCategory.CAREER, "Are you interested in a career in marketing?", R.drawable.ic_figure_2),
//        Question(6, QuestionCategory.CAREER, "Are you interested in a career in human resources?", R.drawable.ic_figure_2),
//
//        // Personal Qualities
//        Question(7, QuestionCategory.PERSONAL, "Are you ambitious?", R.drawable.ic_figure_1),
//        Question(8, QuestionCategory.PERSONAL, "Are you hardworking?", R.drawable.ic_figure_1),
//        Question(9, QuestionCategory.PERSONAL, "Are you persistent?", R.drawable.ic_figure_1)
//    )

    val totalQuestions: Int
        get() = _questions.value.size

    val currentQuestion: StateFlow<Question> = combine(_questions, _currentQuestionIndex) { questions, index ->
        questions.getOrNull(index) ?: Question(0, QuestionCategory.GENERAL, "Loading...", R.drawable.ic_placeholder)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Question(0, QuestionCategory.GENERAL, "Loading...", R.drawable.ic_placeholder))

    val selectedAnswer: StateFlow<String?> = combine(_currentQuestionIndex, _answers, _questions) { index, answers, questions ->
        val questionId = questions.getOrNull(index)?.id
        questionId?.let { answers[it] }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        fetchQuestions()
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
                            imageResId = R.drawable.ic_figure_1 // Update if category-based image mapping needed
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
        fetchQuestions()
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
            ?: Question(0, QuestionCategory.GENERAL, "Loading...", R.drawable.ic_placeholder)
    }

    fun getCurrentCategoryProgress(): Pair<Int, Int> {
        val currentQuestionId = currentQuestion.value.id
        val currentCategory = currentQuestion.value.category

        val questionsInCategory = _questions.value.filter { it.category == currentCategory }
        val indexInCategory = questionsInCategory.indexOfFirst { it.id == currentQuestionId }

        return Pair(indexInCategory + 1, questionsInCategory.size)
    }

}