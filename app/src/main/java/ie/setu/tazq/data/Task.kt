package ie.setu.tazq.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ie.setu.tazq.ui.components.task.TaskPriority
import java.util.Date

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "Untitled",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val description: String = "No description",
    val category: String = "Personal",
    val dateCreated: Date = Date(),
    val isDone: Boolean = false
)
