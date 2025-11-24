package ru.practicum.android.diploma.designSystem.uiKit

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalColorScheme = staticCompositionLocalOf { lightColorScheme }
val LocalTypography = staticCompositionLocalOf { vacancyTypography }
val LocalShapes = staticCompositionLocalOf { vacancyRoundedShapes }
val LocalIconTintColor = compositionLocalOf { Color.Black }
val IsDarkMode = staticCompositionLocalOf { false }

object VacancyTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: VacancyTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: VacancyShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current

    val isDarkMode: Boolean
        @Composable
        @ReadOnlyComposable
        get() = IsDarkMode.current
}

@Composable
fun VacancyTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    shapes: VacancyShapes = VacancyTheme.shapes,
    typography: VacancyTypography = VacancyTheme.typography,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val tintColor = colorScheme.onSurfaceVariant
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = isDarkTheme
                isAppearanceLightNavigationBars = isDarkTheme
            }
        }
    }
    MaterialTheme {
        CompositionLocalProvider(
            LocalColorScheme provides colorScheme,
            LocalShapes provides shapes,
            LocalTypography provides typography,
            LocalIconTintColor provides tintColor,
            IsDarkMode provides isDarkTheme
        ) {
            ProvideTextStyle(value = typography.regular16, content = content)
        }
    }
}
