package ru.practicum.android.diploma.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class VacancyTypography(
    val bold32: TextStyle,
    val medium22: TextStyle,
    val medium16: TextStyle,
    val regular16: TextStyle,
    val regular12: TextStyle
)

val systemFontFamily = FontFamily.Default

val vacancyTypography = VacancyTypography(
    bold32 = TextStyle(
        fontFamily = systemFontFamily,
        fontWeight = FontWeight.Bold, // 700
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    medium22 = TextStyle(
        fontFamily = systemFontFamily,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    medium16 = TextStyle(
        fontFamily = systemFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    regular16 = TextStyle(
        fontFamily = systemFontFamily,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    regular12 = TextStyle(
        fontFamily = systemFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
