package ru.practicum.android.diploma.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

data class TeamMemberUI(
    val name: String,
    val role: String,
    val iconResId: Int? = null
)

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
            text = "Над приложением работали",
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

@Composable
fun TeamMemberItem(
    member: TeamMemberUI
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Возможность сделать иконки
        // if (member.iconResId != null) {
        //     Image(
        //         painter = painterResource(id = member.iconResId),
        //         contentDescription = null,
        //         modifier = Modifier
        //             .size(40.dp)
        //             .padding(end = 12.dp)
        //     )
        // } else {
        //     // Заглушка для иконки
        //     Spacer(modifier = Modifier
        //         .size(40.dp)
        //         .padding(end = 12.dp)
        //         .background(
        //             color = VacancyTheme.colorScheme.primary.copy(alpha = 0.1f),
        //             shape = CircleShape
        //         )
        //     )
        // }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = member.name,
                style = VacancyTheme.typography.medium16,
                color = VacancyTheme.colorScheme.inverseSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = member.role,
                style = VacancyTheme.typography.regular12,
                color = VacancyTheme.colorScheme.inverseSurface.copy(alpha = 0.6f)
            )
        }
    }
}

