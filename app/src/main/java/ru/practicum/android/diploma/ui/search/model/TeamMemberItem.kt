package ru.practicum.android.diploma.ui.search.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

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
