package ru.practicum.android.diploma.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

@Composable
fun SearchScreen(
    navController: NavController,
    onClearSearchText: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 19.dp)
            .background(VacancyTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.vacancies_search),
                style = VacancyTheme.typography.medium22,
                color = VacancyTheme.colorScheme.inverseSurface
            )

            IconButton(
                onClick = {
                    // TODO: переход к экрану фильтрации, когда появится навигация
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                    tint = VacancyTheme.colorScheme.inverseSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.enter_search_request),
                    style = VacancyTheme.typography.regular16,
                    color = VacancyTheme.colorScheme.onBackground
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (searchQuery.isEmpty()) Icons.Filled.Search else Icons.Filled.Clear,
                    contentDescription = null,
                    tint = VacancyTheme.colorScheme.onPrimaryContainer,
                )
            },
            shape = VacancyTheme.shapes.shape10dp,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VacancyTheme.colorScheme.secondaryContainer,
                unfocusedBorderColor = VacancyTheme.colorScheme.secondaryContainer,
                cursorColor = VacancyTheme.colorScheme.primary,
                focusedContainerColor = VacancyTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = VacancyTheme.colorScheme.secondaryContainer
            )
        )


        if (searchQuery.isEmpty()) SearchPlaceholder()

    }
}

@Composable
fun SearchPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.placeholder_search),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .width(328.dp)
                .height(223.dp),
            contentScale = ContentScale.Fit
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    VacancyTheme(isDarkTheme = true) {
        SearchScreen(navController = NavController(LocalContext.current), onClearSearchText = {})
    }
}
