package ru.practicum.android.diploma.ui.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

@Composable
fun VacancyDetailsScreen(
    vacancyId: String,
    navController: NavController
) {
    Text(text = stringResource(R.string.vacancy), color = VacancyTheme.colorScheme.onBackground)
}
