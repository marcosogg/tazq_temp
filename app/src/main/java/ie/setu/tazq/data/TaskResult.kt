package ie.setu.tazq.data

sealed class TaskResult<out T> {
    data class Success<T>(val data: T) : TaskResult<T>()
    data class Error(val exception: Exception) : TaskResult<Nothing>()
}
