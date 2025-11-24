package ru.practicum.android.diploma.designSystem.uiKit

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class VacancyShapes(
    // shape
    val shape2dp: CornerBasedShape,
    val shape4dp: CornerBasedShape,
    val shape6dp: CornerBasedShape,
    val shape8dp: CornerBasedShape,
    val shape10dp: CornerBasedShape,
    val shape12dp: CornerBasedShape,
    val shape14dp: CornerBasedShape,
    val shape16dp: CornerBasedShape,
    val shape20dp: CornerBasedShape,
    val shape24dp: CornerBasedShape,
    val shape32dp: CornerBasedShape,

    // shapeTop
    val shapeTop8dp: CornerBasedShape,
    val shapeTop12dp: CornerBasedShape,
    val shapeTop16dp: CornerBasedShape,

    // shapeBottom
    val shapeBottom8dp: CornerBasedShape,
    val shapeBottom4dp: CornerBasedShape,
    val shapeBottom12dp: CornerBasedShape,
    val shapeBottom16dp: CornerBasedShape,
)

val vacancyRoundedShapes = VacancyShapes(
    // shape
    shape2dp = RoundedCornerShape(2.dp),
    shape4dp = RoundedCornerShape(4.dp),
    shape6dp = RoundedCornerShape(6.dp),
    shape8dp = RoundedCornerShape(8.dp),
    shape10dp = RoundedCornerShape(10.dp),
    shape12dp = RoundedCornerShape(12.dp),
    shape14dp = RoundedCornerShape(14.dp),
    shape16dp = RoundedCornerShape(16.dp),
    shape20dp = RoundedCornerShape(20.dp),
    shape24dp = RoundedCornerShape(24.dp),
    shape32dp = RoundedCornerShape(32.dp),

    // shapeTop
    shapeTop8dp = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp),
    shapeTop12dp = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp),
    shapeTop16dp = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),

    // shapeBottom
    shapeBottom4dp = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp),
    shapeBottom8dp = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp),
    shapeBottom12dp = RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp),
    shapeBottom16dp = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
)
