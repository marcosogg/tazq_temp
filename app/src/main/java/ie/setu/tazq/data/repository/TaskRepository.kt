package ie.setu.tazq.data.repository

import ie.setu.tazq.data.Task
import ie.setu.tazq.data.room.TaskDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDAO: TaskDAO) {
    fun getAll(): Flow<List<Task>> = taskDAO.getAll()

    fun get(id: Int): Flow<Task> = taskDAO.get(id)

    suspend fun insert(task: Task) { taskDAO.insert(task) }

    suspend fun update(task: Task) { taskDAO.update(task) }

    suspend fun delete(task: Task) { taskDAO.delete(task) }

    suspend fun updateTaskStatus(id: Int, isDone: Boolean) {
        taskDAO.updateTaskStatus(id, isDone)
    }
}
