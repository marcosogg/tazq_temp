package ie.setu.tazq.ui.screens.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.setu.tazq.data.Task
import ie.setu.tazq.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    // Backing property for tasks
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    // Public immutable flow of tasks
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // Initialize the ViewModel by collecting tasks from repository
    init {
        viewModelScope.launch {
            repository.getAll().collect { listOfTasks ->
                _tasks.value = listOfTasks
            }
        }
    }

    // Delete a task
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
            // No need to update _tasks manually as Flow will trigger automatically
        }
    }

    // Toggle task completion status
    fun updateTaskStatus(task: Task) {
        viewModelScope.launch {
            repository.updateTaskStatus(task.id, !task.isDone)
            // No need to update _tasks manually as Flow will trigger automatically
        }
    }

    // Update task
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
            // No need to update _tasks manually as Flow will trigger automatically
        }
    }

    // Optional: Add search functionality
    fun searchTasks(query: String) {
        viewModelScope.launch {
            repository.getAll().collect { allTasks ->
                _tasks.value = allTasks.filter { task ->
                    task.title.contains(query, ignoreCase = true) ||
                            task.description.contains(query, ignoreCase = true)
                }
            }
        }
    }

    // Optional: Add sorting functionality
    fun sortTasks(sortBy: SortOption) {
        viewModelScope.launch {
            val currentTasks = _tasks.value
            _tasks.value = when (sortBy) {
                SortOption.DATE -> currentTasks.sortedByDescending { it.dateCreated }
                SortOption.PRIORITY -> currentTasks.sortedBy { it.priority }
                SortOption.CATEGORY -> currentTasks.sortedBy { it.category }
            }
        }
    }
}

// Enum for sorting options
enum class SortOption {
    DATE,
    PRIORITY,
    CATEGORY
}
