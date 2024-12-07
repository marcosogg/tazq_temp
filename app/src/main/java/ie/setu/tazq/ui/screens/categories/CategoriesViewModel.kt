package ie.setu.tazq.ui.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ie.setu.tazq.data.Categories
import ie.setu.tazq.data.Task
import ie.setu.tazq.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll().collect { listOfTasks ->
                _tasks.value = listOfTasks
            }
        }
    }

    fun getCategories() = Categories.list

    fun getCategoryCounts(): Map<String, Int> {
        val categoryCounts = mutableMapOf<String, Int>()
        Categories.list.forEach { category ->
            categoryCounts[category.name] = _tasks.value.count { it.category == category.name }
        }
        return categoryCounts
    }
}
