// ui/screens/task/TaskViewModel.kt
package ie.setu.tazq.ui.screens.task

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.setu.tazq.data.Task
import ie.setu.tazq.data.repository.TaskRepository
import ie.setu.tazq.ui.components.task.TaskPriority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Task title state
    private val _taskTitle = MutableStateFlow("")
    val taskTitle: StateFlow<String> = _taskTitle.asStateFlow()

    // Task description state
    private val _taskDescription = MutableStateFlow("")
    val taskDescription: StateFlow<String> = _taskDescription.asStateFlow()

    // Task priority state
    private val _taskPriority = MutableStateFlow(TaskPriority.MEDIUM)
    val taskPriority: StateFlow<TaskPriority> = _taskPriority.asStateFlow()

    // Selected category state
    private val _selectedCategory = MutableStateFlow("Personal")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Validation states
    private val _isTitleValid = MutableStateFlow(false)
    val isTitleValid: StateFlow<Boolean> = _isTitleValid.asStateFlow()

    private val _isDescriptionValid = MutableStateFlow(true) // Initially true as empty description is valid
    val isDescriptionValid: StateFlow<Boolean> = _isDescriptionValid.asStateFlow()

    // Field interaction states
    private val _titleTouched = mutableStateOf(false)
    val titleTouched: State<Boolean> = _titleTouched

    private val _descriptionTouched = mutableStateOf(false)
    val descriptionTouched: State<Boolean> = _descriptionTouched

    // UI state
    private val _showConfirmation = MutableStateFlow(false)
    val showConfirmation: StateFlow<Boolean> = _showConfirmation.asStateFlow()

    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateTaskTitle(title: String) {
        _taskTitle.value = title
        _titleTouched.value = true
        validateTitle(title)
    }

    fun updateTaskDescription(description: String) {
        _taskDescription.value = description
        _descriptionTouched.value = true
        validateDescription(description)
    }

    fun updateTaskPriority(priority: TaskPriority) {
        _taskPriority.value = priority
    }

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    private fun validateTitle(title: String) {
        _isTitleValid.value = title.isNotEmpty() && title.length >= 3
    }

    private fun validateDescription(description: String) {
        _isDescriptionValid.value = description.length <= 500
    }

    fun isFormValid(): Boolean {
        return _isTitleValid.value && _isDescriptionValid.value
    }

    fun getTitleErrorMessage(): String? {
        return when {
            !_titleTouched.value -> null
            _taskTitle.value.isEmpty() -> "Title cannot be empty"
            _taskTitle.value.length < 3 -> "Title must be at least 3 characters"
            else -> null
        }
    }

    fun getDescriptionErrorMessage(): String? {
        return when {
            !_descriptionTouched.value -> null
            _taskDescription.value.length > 500 -> "Description must be less than 500 characters"
            else -> null
        }
    }

    fun addTask() {
        if (!isFormValid()) {
            _errorMessage.value = "Please fix the errors before submitting"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val newTask = Task(
                    title = _taskTitle.value,
                    description = _taskDescription.value,
                    priority = _taskPriority.value,
                    category = _selectedCategory.value
                )

                repository.insert(newTask)
                _showConfirmation.value = true
                resetForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to create task"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun resetForm() {
        _taskTitle.value = ""
        _taskDescription.value = ""
        _taskPriority.value = TaskPriority.MEDIUM
        _selectedCategory.value = "Personal"
        _isTitleValid.value = false
        _isDescriptionValid.value = true
        _titleTouched.value = false
        _descriptionTouched.value = false
        _errorMessage.value = null
    }

    fun hideConfirmation() {
        _showConfirmation.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // Optional: Preview data for testing
    fun setPreviewData(task: Task) {
        _taskTitle.value = task.title
        _taskDescription.value = task.description
        _taskPriority.value = task.priority
        _selectedCategory.value = task.category
        validateTitle(task.title)
        validateDescription(task.description)
    }
}
