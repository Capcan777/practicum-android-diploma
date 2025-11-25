package ru.practicum.android.diploma.ui.about

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

@Composable
fun AboutTeamScreen(
    navController: NavController
) {
    Text(text = stringResource(R.string.team), color = VacancyTheme.colorScheme.onBackground)
}
