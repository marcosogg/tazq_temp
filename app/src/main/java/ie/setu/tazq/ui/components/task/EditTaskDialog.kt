package ie.setu.tazq.ui.components.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.setu.tazq.data.Task

@Composable
fun EditTaskDialog(
    task: Task,
    onDismissRequest: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var editedPriority by remember { mutableStateOf(task.priority) }
    var editedCategory by remember { mutableStateOf(task.category) }

    // Validation states
    var isTitleValid by remember { mutableStateOf(true) }
    var isDescriptionValid by remember { mutableStateOf(true) }

    // Error messages
    var titleErrorMessage by remember { mutableStateOf<String?>(null) }
    var descriptionErrorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Edit Task") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskInput(
                    value = editedTitle,
                    onTaskTitleChange = {
                        editedTitle = it
                        isTitleValid = it.length >= 3
                        titleErrorMessage = if (!isTitleValid) {
                            "Title must be at least 3 characters"
                        } else null
                    },
                    isError = !isTitleValid,
                    errorMessage = titleErrorMessage
                )

                TaskDescription(
                    value = editedDescription,
                    onDescriptionChange = {
                        editedDescription = it
                        isDescriptionValid = it.length <= 500
                        descriptionErrorMessage = if (!isDescriptionValid) {
                            "Description must be less than 500 characters"
                        } else null
                    },
                    isError = !isDescriptionValid,
                    errorMessage = descriptionErrorMessage
                )

                RadioButtonGroup(
                    selectedPriority = editedPriority,
                    onPriorityChange = { editedPriority = it }
                )

                CategoryDropdown(
                    selectedCategory = editedCategory,
                    onCategorySelected = { editedCategory = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isTitleValid && isDescriptionValid) {
                        onConfirm(
                            task.copy(
                                title = editedTitle,
                                description = editedDescription,
                                priority = editedPriority,
                                category = editedCategory
                            )
                        )
                        onDismissRequest()
                    }
                },
                enabled = isTitleValid && isDescriptionValid
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
