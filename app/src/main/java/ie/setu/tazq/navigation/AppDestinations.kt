package ie.setu.tazq.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.ui.graphics.vector.ImageVector

interface AppDestination {
    val icon: ImageVector
    val label: String
    val route: String
}

object TaskList : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val label = "Tasks"
    override val route = "tasklist"
}

object CreateTask : AppDestination {
    override val icon = Icons.Default.Add
    override val label = "New Task"
    override val route = "createtask"
}

object Categories : AppDestination {
    override val icon = Icons.Default.Category
    override val label = "Categories"
    override val route = "categories"
}

object About : AppDestination {
    override val icon = Icons.Default.Info
    override val label = "About"
    override val route = "about"
}

object SignIn : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.Login
    override val label = "Sign In"
    override val route = "signin"
}

object SignUp : AppDestination {
    override val icon = Icons.Default.PersonAdd
    override val label = "Sign Up"
    override val route = "signup"
}

val bottomAppBarDestinations = listOf(TaskList, CreateTask, Categories, About)
val allDestinations = listOf(TaskList, CreateTask, Categories, About)
