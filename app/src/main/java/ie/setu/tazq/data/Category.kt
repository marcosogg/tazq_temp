package ie.setu.tazq.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val name: String,
    val icon: ImageVector,
    var taskCount: Int = 0
)

object Categories {
    val list = listOf(
        Category("Work", Icons.Default.Work),
        Category("Personal", Icons.Default.Person),
        Category("Shopping", Icons.Default.ShoppingCart),
        Category("Health", Icons.Default.Favorite),
        Category("Study", Icons.Default.School)
    )

    fun getIcon(categoryName: String): ImageVector {
        return list.find { it.name == categoryName }?.icon ?: Icons.AutoMirrored.Filled.Label
    }
}
