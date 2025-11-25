package ru.practicum.android.diploma.ui.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

@Composable
fun SearchScreen(
    navController: NavController
) {
    Text(text = stringResource(R.string.vacancies_search), color = VacancyTheme.colorScheme.onBackground)
}
