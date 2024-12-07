package ie.setu.tazq.ui.screens.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ie.setu.tazq.data.Task
import ie.setu.tazq.ui.components.task.EditTaskDialog
import ie.setu.tazq.ui.components.tasklist.TaskList

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {},
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showSortMenu by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    // Show edit dialog when taskToEdit is not null
    taskToEdit?.let { task ->
        EditTaskDialog(
            task = task,
            onDismissRequest = { taskToEdit = null },
            onConfirm = { editedTask ->
                viewModel.updateTask(editedTask)
                taskToEdit = null
            }
        )
    }

    Column(
        modifier = modifier.padding(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        // Header with Search and Sort
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Tasks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                // Search Icon
                IconButton(onClick = { showSearchBar = !showSearchBar }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Tasks"
                    )
                }

                // Sort Menu
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort Tasks"
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Sort by Date") },
                        onClick = {
                            viewModel.sortTasks(SortOption.DATE)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sort by Priority") },
                        onClick = {
                            viewModel.sortTasks(SortOption.PRIORITY)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sort by Category") },
                        onClick = {
                            viewModel.sortTasks(SortOption.CATEGORY)
                            showSortMenu = false
                        }
                    )
                }
            }
        }

        // Search Bar
        if (showSearchBar) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchTasks(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("Search tasks...") },
                singleLine = true
            )
        }

        if (tasks.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No tasks yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Add a task to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Task list
            TaskList(
                modifier = Modifier.fillMaxSize(),
                tasks = tasks,
                onDeleteTask = { task -> viewModel.deleteTask(task) },
                onToggleTask = { task -> viewModel.updateTaskStatus(task) },
                onEditTask = { task -> taskToEdit = task }
            )
        }
    }
}
