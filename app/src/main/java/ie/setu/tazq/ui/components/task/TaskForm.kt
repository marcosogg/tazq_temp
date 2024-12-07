package ie.setu.tazq.ui.components.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.tazq.data.Task

@Composable
fun TaskForm(
    modifier: Modifier = Modifier,
    onTaskCreated: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var taskDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Personal") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TaskInput(
            value = taskTitle,
            onTaskTitleChange = { taskTitle = it },
            isError = taskTitle.isNotEmpty() && taskTitle.length < 3,
            errorMessage = if (taskTitle.isNotEmpty() && taskTitle.length < 3) {
                "Title must be at least 3 characters"
            } else null
        )

        RadioButtonGroup(
            selectedPriority = taskPriority,
            onPriorityChange = { taskPriority = it }
        )

        CategoryDropdown(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier.fillMaxWidth()
        )

        TaskDescription(
            value = taskDescription,
            onDescriptionChange = { taskDescription = it },
            isError = taskDescription.length > 500,
            errorMessage = if (taskDescription.length > 500) {
                "Description must be less than 500 characters"
            } else null
        )

        Button(
            onClick = {
                if (taskTitle.length >= 3 && taskDescription.length <= 500) {
                    val newTask = Task(
                        title = taskTitle,
                        priority = taskPriority,
                        description = taskDescription,
                        category = selectedCategory
                    )
                    onTaskCreated(newTask)
                    // Reset form
                    taskTitle = ""
                    taskDescription = ""
                    taskPriority = TaskPriority.MEDIUM
                    selectedCategory = "Personal"
                }
            },
            enabled = taskTitle.length >= 3 && taskDescription.length <= 500,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
    }
}
