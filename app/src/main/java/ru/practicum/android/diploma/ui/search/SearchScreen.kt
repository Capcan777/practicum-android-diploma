package ru.practicum.android.diploma.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.common.Placeholder
import ru.practicum.android.diploma.ui.search.state.SearchScreenState
import ru.practicum.android.diploma.ui.search.state.VacancyUiModel

@Composable
fun SearchScreen(
    navController: NavController,
    onClearSearchText: () -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsState(initial = SearchScreenState.Nothing)
    val vacancies = (screenState as? SearchScreenState.Content)?.vacancies ?: emptyList()
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
                    // переход к экрану фильтрации, когда появится навигация
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
            onValueChange = {
                searchQuery = it
                viewModel.onSearchTextChanged(searchQuery)
            },
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
                    modifier = Modifier.clickable {
                        if (searchQuery.isNotEmpty()) {
                            searchQuery = ""
                            onClearSearchText()
                            viewModel.clearCountVacancies()


                        }
                    }
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

        when (val state = screenState) {
            is SearchScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchScreenState.Content -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                ) {
                    CountVacancies(
                        screenState,
                        vacancies,
                        textMessage = stringResource(R.string.found_count_vacancies, vacancies.size)
                    )
                }
                VacancyListItem(vacancies = state.vacancies, onItemClick = {
                    navController.navigate("vacancyDetails")
                })
            }

            is SearchScreenState.Error.NoConnection -> {
                Placeholder(
                    imageResId = R.drawable.placeholder_no_connection,
                    title = stringResource(R.string.no_connection)
                )
            }

            is SearchScreenState.Error.ServerError -> {
                // показать экран ошибки сервера
            }

            is SearchScreenState.Error.NotFound -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                ) {
                    CountVacancies(
                        screenState,
                        vacancies,
                        textMessage = "Таких вакансий нет"
                    )
                }
                Placeholder(
                    imageResId = R.drawable.placeholder_error_riecive,
                    title = stringResource(R.string.not_found)
                )
            }

            is SearchScreenState.Error -> {
                // показать экран ошибки
            }

            is SearchScreenState.Nothing -> {
                // ничего не делать
            }
        }
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

@Composable
fun VacancyListItem(
    vacancies: List<VacancyUiModel>,
    onItemClick: (Vacancy) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        items(vacancies) { uiVacancy ->
            VacancyRow(
                vacancyUiModel = uiVacancy,
                onClick = { onItemClick(uiVacancy.vacancy) }
            )
        }
    }
}

@Composable
fun VacancyRow(
    vacancyUiModel: VacancyUiModel,
    onClick: () -> Unit
) {
    val imageLoader: ImageLoader = koinInject()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        val logoUrl = vacancyUiModel.vacancy.company.logoUrl
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(logoUrl)
                .build(),
            imageLoader = imageLoader,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.placeholder_logo),
            modifier = Modifier
                .size(48.dp)
                .clip(VacancyTheme.shapes.shape12dp),
            contentScale = ContentScale.Inside
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = "${vacancyUiModel.vacancy.title}, ${vacancyUiModel.vacancy.location}",
                style = VacancyTheme.typography.medium22,
                color = VacancyTheme.colorScheme.inverseSurface,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = vacancyUiModel.vacancy.company.name,
                style = VacancyTheme.typography.regular16,
                color = VacancyTheme.colorScheme.inverseSurface

            )
            Text(
                text = "${vacancyUiModel.vacancy.salary?.from}",
                style = VacancyTheme.typography.regular16,
                color = VacancyTheme.colorScheme.inverseSurface
            )
        }
    }
}

@Composable
fun CountVacancies(
    screenState: SearchScreenState,
    vacancies: List<VacancyUiModel>,
    textMessage: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = VacancyTheme.colorScheme.primary,
                    shape = VacancyTheme.shapes.shape12dp
                )
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = textMessage,
                color = VacancyTheme.colorScheme.onPrimary,
                style = VacancyTheme.typography.regular16,
            )
        }
    }
}
