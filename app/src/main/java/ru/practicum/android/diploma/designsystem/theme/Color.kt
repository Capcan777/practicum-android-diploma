package ru.practicum.android.diploma.designsystem.theme

import androidx.compose.ui.graphics.Color

private const val BLUE_PRIMARY = 0xFF3772E7
private const val WHITE_UNIVERSAL = 0xFFFFFFFF
private const val BLACK_UNIVERSAL = 0xFF1A1B22
private const val LIGHT_GRAY = 0xFFE6E8EB
private const val GRAY_SECONDARY = 0xFFAEAFB4
private const val RED_ERROR = 0xFFF56B6C
private const val DARK_SURFACE_VARIANT = 0xFF45464F

private const val ERROR_CONTAINER_LIGHT = 0xFFFFDAD6
private const val ON_ERROR_CONTAINER_LIGHT = 0xFF410002
private const val ERROR_CONTAINER_DARK = 0xFF93000A
private const val ON_ERROR_CONTAINER_DARK = 0xFFFFDAD6
private const val OUTLINE_LIGHT = 0xFF767680
private const val OUTLINE_VARIANT_LIGHT = 0xFFC6C6D0
private const val OUTLINE_DARK = 0xFF8A9297
private const val OUTLINE_VARIANT_DARK = 0xFF41484D
private const val SURFACE_VARIANT_LIGHT = 0xFFE6E8EB
private const val ON_SURFACE_VARIANT_LIGHT = 0xFF45464F
private const val ON_SURFACE_VARIANT_DARK = 0xFFC6C6D0

private const val PURE_BLACK = 0xFF000000
private const val PURE_WHITE = 0xFFFFFFFF

// Light theme
val primaryLight = Color(BLUE_PRIMARY) // Blue
val onPrimaryLight = Color(WHITE_UNIVERSAL) // White Universal
val primaryContainerLight = Color(LIGHT_GRAY) // Light Gray
val onPrimaryContainerLight = Color(BLACK_UNIVERSAL) // Black Universal

val secondaryLight = Color(GRAY_SECONDARY) // Gray
val onSecondaryLight = Color(WHITE_UNIVERSAL) // White Universal
val secondaryContainerLight = Color(LIGHT_GRAY) // Light Gray
val onSecondaryContainerLight = Color(BLACK_UNIVERSAL) // Black Universal

val errorLight = Color(RED_ERROR) // Red
val onErrorLight = Color(WHITE_UNIVERSAL) // White Universal
val errorContainerLight = Color(ERROR_CONTAINER_LIGHT)
val onErrorContainerLight = Color(ON_ERROR_CONTAINER_LIGHT)

val backgroundLight = Color(WHITE_UNIVERSAL) // White[Day]
val onBackgroundLight = Color(BLACK_UNIVERSAL) // Black[Day]

val surfaceLight = Color(WHITE_UNIVERSAL) // White[Day]
val onSurfaceLight = Color(BLACK_UNIVERSAL) // Black[Day]

val surfaceVariantLight = Color(SURFACE_VARIANT_LIGHT) // Light Gray
val onSurfaceVariantLight = Color(ON_SURFACE_VARIANT_LIGHT)

val outlineLight = Color(OUTLINE_LIGHT)
val outlineVariantLight = Color(OUTLINE_VARIANT_LIGHT)

// Dark theme
val primaryDark = Color(BLUE_PRIMARY) // Blue
val onPrimaryDark = Color(BLACK_UNIVERSAL) // Black Universal
val primaryContainerDark = Color(DARK_SURFACE_VARIANT)
val onPrimaryContainerDark = Color(WHITE_UNIVERSAL) // White Universal

val secondaryDark = Color(GRAY_SECONDARY) // Gray
val onSecondaryDark = Color(BLACK_UNIVERSAL) // Black Universal
val secondaryContainerDark = Color(DARK_SURFACE_VARIANT)
val onSecondaryContainerDark = Color(WHITE_UNIVERSAL) // White Universal

val errorDark = Color(RED_ERROR) // Red
val onErrorDark = Color(BLACK_UNIVERSAL) // Black Universal
val errorContainerDark = Color(ERROR_CONTAINER_DARK)
val onErrorContainerDark = Color(ON_ERROR_CONTAINER_DARK)

val backgroundDark = Color(BLACK_UNIVERSAL) // Black[Night]
val onBackgroundDark = Color(WHITE_UNIVERSAL) // White[Night]

val surfaceDark = Color(BLACK_UNIVERSAL) // Black[Night]
val onSurfaceDark = Color(WHITE_UNIVERSAL) // White[Night]

val surfaceVariantDark = Color(DARK_SURFACE_VARIANT)
val onSurfaceVariantDark = Color(ON_SURFACE_VARIANT_DARK)

val outlineDark = Color(OUTLINE_DARK)
val outlineVariantDark = Color(OUTLINE_VARIANT_DARK)

val pureBlack = Color(PURE_BLACK)
val pureWhite = Color(PURE_WHITE)
