package ie.setu.tazq.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Convert XML colors to Compose Color objects
private val NavyPrimary = Color(0xFF1B3B6F)
private val NavyLight = Color(0xFF2C5494)
private val NavyDark = Color(0xFF102344)
private val SkyBlueAccent = Color(0xFF4A90E2)
private val GrayMedium = Color(0xFF757575)
private val White = Color(0xFFFFFFFF)
private val GrayLight = Color(0xFFF5F5F5)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = White,
    primaryContainer = NavyLight,
    onPrimaryContainer = White,
    secondary = SkyBlueAccent,
    onSecondary = White,
    surface = White,
    onSurface = NavyDark,
    background = GrayLight,
    onBackground = NavyDark
)

@Composable
fun TazqTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}