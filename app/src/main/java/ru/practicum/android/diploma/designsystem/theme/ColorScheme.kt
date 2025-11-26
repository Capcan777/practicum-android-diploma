package ru.practicum.android.diploma.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

private const val ALPHA_08 = 0.08f
private const val ALPHA_12 = 0.12f

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

    scrim = pureBlack,
    inverseSurface = onBackgroundLight,
    inverseOnSurface = backgroundLight,
    inversePrimary = primaryContainerLight,

    surfaceDim = surfaceVariantLight,
    surfaceBright = surfaceLight,
    surfaceContainerLowest = pureWhite,
    surfaceContainerLow = surfaceLight,
    surfaceContainer = surfaceVariantLight,
    surfaceContainerHigh = onSurfaceVariantLight.copy(alpha = ALPHA_08),
    surfaceContainerHighest = onSurfaceVariantLight.copy(alpha = ALPHA_12)
)

val darkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryDark,

    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryLight,
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

    scrim = pureBlack,
    inverseSurface = onBackgroundDark,
    inverseOnSurface = backgroundDark,
    inversePrimary = primaryContainerDark,

    surfaceDim = surfaceVariantDark,
    surfaceBright = surfaceDark,
    surfaceContainerLowest = pureBlack,
    surfaceContainerLow = surfaceDark,
    surfaceContainer = surfaceVariantDark,
    surfaceContainerHigh = onSurfaceVariantDark.copy(alpha = ALPHA_08),
    surfaceContainerHighest = onSurfaceVariantDark.copy(alpha = ALPHA_12)
)
