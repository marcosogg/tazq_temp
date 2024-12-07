package ie.setu.tazq.ui.screens.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()

    Column(
        modifier = modifier.padding(
            top = 48.dp,
            start = 24.dp,
            end = 24.dp
        )
    ) {
        Text(
            text = "Categories",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CategoryList(
            categories = viewModel.getCategories(),
            taskCounts = viewModel.getCategoryCounts()
        )
    }
}
