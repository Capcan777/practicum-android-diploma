package ru.practicum.android.diploma.designsystem.uiKit

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val lightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,

    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,

    tertiary = secondaryLight,
    onTertiary = onSecondaryLight,
    tertiaryContainer = secondaryContainerLight,
    onTertiaryContainer = onSecondaryContainerLight,

    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,

    background = backgroundLight,
    onBackground = onBackgroundLight,

    surface = surfaceLight,
    onSurface = onSurfaceLight,

    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,

    outline = outlineLight,
    outlineVariant = outlineVariantLight,

    scrim = Color(0xFF000000),
    inverseSurface = onBackgroundLight,
    inverseOnSurface = backgroundLight,
    inversePrimary = primaryContainerLight,

    surfaceDim = surfaceVariantLight,
    surfaceBright = surfaceLight,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = surfaceLight,
    surfaceContainer = surfaceVariantLight,
    surfaceContainerHigh = onSurfaceVariantLight.copy(alpha = 0.08f),
    surfaceContainerHighest = onSurfaceVariantLight.copy(alpha = 0.12f)
)

val darkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,

    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,

    tertiary = secondaryDark,
    onTertiary = onSecondaryDark,
    tertiaryContainer = secondaryContainerDark,
    onTertiaryContainer = onSecondaryContainerDark,

    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,

    background = backgroundDark,
    onBackground = onBackgroundDark,

    surface = surfaceDark,
    onSurface = onSurfaceDark,

    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,

    outline = outlineDark,
    outlineVariant = outlineVariantDark,

    scrim = Color(0xFF000000),
    inverseSurface = onBackgroundDark,
    inverseOnSurface = backgroundDark,
    inversePrimary = primaryContainerDark,

    surfaceDim = surfaceVariantDark,
    surfaceBright = surfaceDark,
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = surfaceDark,
    surfaceContainer = surfaceVariantDark,
    surfaceContainerHigh = onSurfaceVariantDark.copy(alpha = 0.08f),
    surfaceContainerHighest = onSurfaceVariantDark.copy(alpha = 0.12f)
)
