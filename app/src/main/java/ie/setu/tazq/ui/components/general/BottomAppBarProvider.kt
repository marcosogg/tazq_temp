package ie.setu.tazq.ui.components.general

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.setu.tazq.navigation.AppDestination
import ie.setu.tazq.navigation.bottomAppBarDestinations
import ie.setu.tazq.ui.theme.TazqTheme

@Composable
fun BottomAppBarProvider(
    navController: NavHostController,
    currentScreen: AppDestination
) {
    var navigationSelectedItem by remember { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        bottomAppBarDestinations.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = navigationItem == currentScreen,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,  // Changed to white
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,  // Changed to white
                    unselectedIconColor = MaterialTheme.colorScheme.primaryContainer,  // Light navy
                    unselectedTextColor = MaterialTheme.colorScheme.primaryContainer,  // Light navy
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer  // Light navy background for selected item
                ),
                label = {
                    Text(text = navigationItem.label)
                },
                icon = {
                    Icon(
                        navigationItem.icon,
                        contentDescription = navigationItem.label
                    )
                },
                onClick = {
                    navigationSelectedItem = index
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarScreenPreview() {
    TazqTheme {
        BottomAppBarProvider(
            rememberNavController(),
            bottomAppBarDestinations[0]
        )
    }
}
