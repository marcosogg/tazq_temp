// navigation/AppNavGraph.kt
package ie.setu.tazq.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.tazq.ui.screens.about.AboutScreen
import ie.setu.tazq.ui.screens.auth.SignInScreen
import ie.setu.tazq.ui.screens.auth.SignUpScreen
import ie.setu.tazq.ui.screens.categories.CategoriesScreen
import ie.setu.tazq.ui.screens.task.TaskScreen
import ie.setu.tazq.ui.screens.tasklist.TaskListScreen

@Composable
fun AppNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
    startDestination: String = SignIn.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        // Auth routes
        composable(route = SignIn.route) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(TaskList.route) {
                        popUpTo(SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(SignUp.route)
                }
            )
        }

        composable(route = SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(TaskList.route) {
                        popUpTo(SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.navigateUp()
                }
            )
        }

        // Main app routes
        composable(route = TaskList.route) {
            TaskListScreen(
                modifier = modifier,
                onSignOut = {
                    navController.navigate(SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = CreateTask.route) {
            TaskScreen(
                modifier = modifier,
                onTaskCreated = {
                    navController.navigate(TaskList.route) {
                        popUpTo(CreateTask.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Categories.route) {
            CategoriesScreen(
                modifier = modifier
            )
        }

        composable(route = About.route) {
            AboutScreen(
                modifier = modifier
            )
        }
    }
}

// Extension function to handle authentication navigation
fun NavHostController.navigateToAuth() {
    navigate(SignIn.route) {
        popUpTo(0) { inclusive = true }
    }
}

// Extension function to handle main app navigation
fun NavHostController.navigateToMain() {
    navigate(TaskList.route) {
        popUpTo(0) { inclusive = true }
    }
}

// Extension function to handle sign out
fun NavHostController.signOut() {
    navigate(SignIn.route) {
        popUpTo(0) { inclusive = true }
    }
}
