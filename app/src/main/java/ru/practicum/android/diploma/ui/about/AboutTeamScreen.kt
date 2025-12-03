package ru.practicum.android.diploma.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.ui.search.model.TeamMemberItem
import ru.practicum.android.diploma.ui.search.model.TeamMemberUI

@Composable
fun AboutTeamScreen(
    navController: NavController
) {
    val teamMembersUI = listOf(
        TeamMemberUI("Тест 1", "Тестирование"),
        TeamMemberUI("Тест 2", "Оценка"),
        TeamMemberUI("Тест 3", "Дизайн"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VacancyTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.team),
            style = VacancyTheme.typography.medium22,
            color = VacancyTheme.colorScheme.inverseSurface,
            modifier = Modifier
                .padding(top = 19.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.team_working_on_app),
            style = VacancyTheme.typography.bold32,
            color = VacancyTheme.colorScheme.inverseSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(teamMembersUI) { member ->
                TeamMemberItem(member = member)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

