package ie.setu.tazq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ie.setu.tazq.navigation.AppNavGraph
import ie.setu.tazq.navigation.SignIn
import ie.setu.tazq.navigation.SignUp
import ie.setu.tazq.navigation.TaskList
import ie.setu.tazq.navigation.allDestinations
import ie.setu.tazq.ui.components.general.BottomAppBarProvider
import ie.setu.tazq.ui.components.general.TopAppBarProvider
import ie.setu.tazq.ui.theme.TazqTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashScreen
            }
        }

        lifecycleScope.launch {
            delay(2000) // 2 seconds splash screen
            keepSplashScreen = false
        }

        setContent {
            TazqTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var startDestination by remember {
                        mutableStateOf(
                            if (auth.currentUser != null) TaskList.route
                            else SignIn.route
                        )
                    }

                    // Listen for auth state changes
                    DisposableEffect(auth) {
                        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                            startDestination = if (firebaseAuth.currentUser != null) {
                                TaskList.route
                            } else {
                                SignIn.route
                            }
                        }
                        auth.addAuthStateListener(authStateListener)

                        onDispose {
                            auth.removeAuthStateListener(authStateListener)
                        }
                    }

                    TazqApp(
                        modifier = Modifier,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}

@Composable
fun TazqApp(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentScreen = allDestinations.find { it.route == currentDestination?.route } ?: TaskList

    // Determine if we should show the bottom bar and top bar
    val shouldShowBars = remember(currentScreen) {
        currentScreen.route != SignIn.route && currentScreen.route != SignUp.route
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (shouldShowBars) {
                TopAppBarProvider(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        content = { paddingValues ->
            AppNavGraph(
                modifier = modifier,
                navController = navController,
                paddingValues = paddingValues,
                startDestination = startDestination
            )
        },
        bottomBar = {
            if (shouldShowBars) {
                BottomAppBarProvider(
                    navController = navController,
                    currentScreen = currentScreen
                )
            }
        }
    )
}

/**
 * Helper function to check if user is authenticated
 */
fun FirebaseAuth.isUserAuthenticated(): Boolean = currentUser != null

/**
 * Helper function to get current user ID safely
 */
fun FirebaseAuth.getCurrentUserId(): String? = currentUser?.uid

/**
 * Helper function to get current user email safely
 */
fun FirebaseAuth.getCurrentUserEmail(): String? = currentUser?.email
