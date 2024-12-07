package ie.setu.tazq.ui.components.task

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskButton(
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit = {}
) {
    Button(
        onClick = onAddTask,
        modifier = modifier.padding(vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Task"
        )
        Text(
            text = "Add Task",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
