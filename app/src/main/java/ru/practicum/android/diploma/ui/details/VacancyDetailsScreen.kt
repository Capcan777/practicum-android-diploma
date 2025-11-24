package ru.practicum.android.diploma.ui.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R

@Composable
fun VacancyDetailsScreen(vacancyId: String) {
    Text(text = stringResource(R.string.vacancy))
}
