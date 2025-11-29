package ru.practicum.android.diploma.ui.search

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
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
    // добавить список поиска вакансий
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
//    formattedSalary: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        val logoUrl = vacancyUiModel.vacancy.company.logoUrl
        Log.d(
            "VacancyRow",
            "logoUrl: $logoUrl, isBlank: ${logoUrl.isBlank()}, isValid: ${
                logoUrl.startsWith("http://") || logoUrl.startsWith("https://")
            }"
        )

        if (logoUrl.isBlank() || (!logoUrl.startsWith("http://") && !logoUrl.startsWith("https://"))) {
            Image(
                painter = painterResource(id = R.drawable.placeholder_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            val context = LocalContext.current
            val imageRequest = remember(logoUrl) {
                ImageRequest.Builder(context)
                    .data(logoUrl)
                    .setHeader(
                        USER_AGENT,
                        VALUE_USER_AGENT
                    )
                    .build()
            }

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder_logo),
                error = painterResource(id = R.drawable.placeholder_logo),
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Inside
            )
        }

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

private const val VALUE_USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
private const val USER_AGENT = "User-Agent"

// @Preview(showSystemUi = true)
// @Composable
// fun SearchScreenPreview() {
//    val vacancies = Vacancy(
//        id = "2222",
//        title = "Engineer в Microsoft",
//        description = "Здесь описание работы",
//        salary = null,
//        company = Employer(
//            id = "2",
//            name = "Microsoft",
//            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1200px-Microsoft_logo.svg.png"
//        ),
//        location = "Москва",
//        industry = Industry(
//            id = 2,
//            name = "Нефтянка"
//        )
//    )
//    VacancyTheme(isDarkTheme = false) {
//        SearchScreen(navController = NavController(LocalContext.current), onClearSearchText = {})
//    }
// }

// @Preview(showBackground = true)
// @Composable
// fun VacancyRowPreview() {
//    val vacancies = VacancyUiModel(Vacancy(
//        id = "2222",
//        title = "fgsfgsfgsfgsfgDevOps Engineer в Microsoft",
//        description = "Здесь описание работы",
//        salary = null,
//        company = Employer(
//            id = "2",
//            name = "Microsoft",
//            logoUrl = "https://upload.wikimedia.org"
//        ),
//        location = "Москва",
//        industry = Industry(
//            id = 2,
//            name = "Нефтянка"
//        ))
//    )
//
//    VacancyTheme(isDarkTheme = false) {
//        VacancyRow(
//            vacancies,
//            onClick = {}
//        )
//    }
// }
// @Preview(showSystemUi = true)
// @Composable
// fun VacancyListItemPreview() {
//    VacancyListItem(
//        vacancies = listOf(
//            Vacancy(
//            id = "2222",
//            title = "fgsfgsfgsfgsfgDevOps Engineer в Microsoft",
//            description = "Здесь описание работы",
//            salary = SalaryRange(
//                from = 1200,
//                to = null,
//                currency = "Руб."
//            ),
//            company = Employer(
//                id = "2",
//                name = "Microsoft",
//                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1200px-Microsoft_logo.svg.png"
//            ),
//            location = "Москва",
//            industry = Industry(
//                id = 2,
//                name = "Нефтянка"
//            )
//            ), Vacancy(
//                id = "2222",
//                title = "fgsfgsfgsfgsfgDevOps Engineer в Microsoft",
//                description = "Здесь описание работы",
//                salary = SalaryRange(
//                    from = 1200,
//                    to = null,
//                    currency = "Руб."
//                ),
//                company = Employer(
//                    id = "2",
//                    name = "Microsoft",
//                    logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/1200px-Microsoft_logo.svg.png"
//                ),
//                location = "Москва",
//                industry = Industry(
//                    id = 2,
//                    name = "Нефтянка"
//                )
//            )
//        ), onItemClick = {})
// }
