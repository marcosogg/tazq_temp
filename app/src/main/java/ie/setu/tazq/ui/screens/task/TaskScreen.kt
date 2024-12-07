// ui/screens/task/TaskScreen.kt
package ie.setu.tazq.ui.screens.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ie.setu.tazq.ui.components.task.CategoryDropdown
import ie.setu.tazq.ui.components.task.RadioButtonGroup
import ie.setu.tazq.ui.components.task.TaskDescription
import ie.setu.tazq.ui.components.task.TaskInput
import ie.setu.tazq.ui.theme.TazqTheme

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    onTaskCreated: () -> Unit = {},
    viewModel: TaskViewModel = hiltViewModel()
) {
    // Collect all states
    val taskTitle by viewModel.taskTitle.collectAsState()
    val taskDescription by viewModel.taskDescription.collectAsState()
    val taskPriority by viewModel.taskPriority.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showConfirmation by viewModel.showConfirmation.collectAsState()
    val isTitleValid by viewModel.isTitleValid.collectAsState()
    val isDescriptionValid by viewModel.isDescriptionValid.collectAsState()
    val titleTouched by viewModel.titleTouched
    val descriptionTouched by viewModel.descriptionTouched

    // Context for showing Toast messages
    val context = LocalContext.current

    // Handle successful task creation
    LaunchedEffect(showConfirmation) {
        if (showConfirmation) {
            onTaskCreated()
        }
    }

    // Main content
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Section
            TaskInput(
                value = taskTitle,
                onTaskTitleChange = viewModel::updateTaskTitle,
                isError = titleTouched && !isTitleValid,
                errorMessage = if (titleTouched) viewModel.getTitleErrorMessage() else null
            )

            // Priority Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    RadioButtonGroup(
                        selectedPriority = taskPriority,
                        onPriorityChange = viewModel::updateTaskPriority
                    )
                }
            }

            // Category Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    CategoryDropdown(
                        selectedCategory = selectedCategory,
                        onCategorySelected = viewModel::updateSelectedCategory,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Description Section
            TaskDescription(
                value = taskDescription,
                onDescriptionChange = viewModel::updateTaskDescription,
                isError = descriptionTouched && !isDescriptionValid,
                errorMessage = if (descriptionTouched) viewModel.getDescriptionErrorMessage() else null
            )

            // Add Task Button
            Button(
                onClick = viewModel::addTask,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                enabled = viewModel.isFormValid() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Add Task")
                }
            }
        }

        // Loading Overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error Message
        AnimatedVisibility(
            visible = errorMessage != null,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = viewModel::clearError) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }

        // Success Dialog
        if (showConfirmation) {
            AlertDialog(
                onDismissRequest = viewModel::hideConfirmation,
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = {
                    Text("Task Created!")
                },
                text = {
                    Text("Your task has been added successfully.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.hideConfirmation()
                            onTaskCreated()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    TazqTheme {
        TaskScreen()
    }
}
