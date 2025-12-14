package ru.practicum.android.diploma.ui.search

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.navigation.Routes
import ru.practicum.android.diploma.ui.common.Placeholder
import ru.practicum.android.diploma.ui.search.state.SearchScreenState
import ru.practicum.android.diploma.ui.search.state.VacancyUiModel
import ru.practicum.android.diploma.util.SalaryDisplay

@Composable
fun SearchScreen(
    navController: NavController,
    onClearSearchText: () -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner? = null
) {
    val viewModel: SearchViewModel = if (viewModelStoreOwner != null) {
        koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
    } else {
        koinViewModel()
    }
    val screenState by viewModel.screenState.collectAsState(initial = SearchScreenState.Nothing)
    val vacancies = (screenState as? SearchScreenState.Content)?.vacancies ?: emptyList()
    val searchTextFromViewModel by viewModel.searchText.collectAsState()
    var searchQuery by rememberSaveable() { mutableStateOf(searchTextFromViewModel) }

    LaunchedEffect(searchTextFromViewModel) {
        if (searchQuery != searchTextFromViewModel) {
            searchQuery = searchTextFromViewModel
        }
    }
    val isLoadingNextPage by viewModel.isLoadingNextPage.collectAsState()
    val hasMorePages by viewModel.hasMorePages.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val filterParameters by viewModel.filterParameters.collectAsState()

    val areFiltersApplied =
        remember(filterParameters) {
            filterParameters.salary.isNotEmpty() ||
                filterParameters.industry.isNotEmpty() ||
                filterParameters.placeOfWork.isNotEmpty() ||
                filterParameters.hideWithoutSalary
        }

    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (!lazyListState.isScrollInProgress) {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = layoutInfo.totalItemsCount

            val isAtEnd = lastVisibleItem?.index == totalItemsCount - 1
            val hasMoreToLoad = hasMorePages && !isLoadingNextPage && vacancies.isNotEmpty()

            if (isAtEnd && hasMoreToLoad) {
                viewModel.loadNextPage()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .background(VacancyTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 19.dp),
                text = stringResource(R.string.vacancies_search),
                style = VacancyTheme.typography.medium22,
                color = VacancyTheme.colorScheme.inverseSurface
            )

            IconButton(
                onClick = {
                    navController.navigate(Routes.SettingsFilter.route)
                }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (areFiltersApplied) VacancyTheme.colorScheme.primary else Color.Transparent)
                        .padding(2.dp)

                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = null,
                        tint = if (areFiltersApplied) {
                            VacancyTheme.colorScheme.onPrimary
                        } else {
                            VacancyTheme.colorScheme.inverseSurface
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                viewModel.onSearchTextChanged(newValue)
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
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            if (searchQuery.isNotEmpty()) {
                                searchQuery = ""
                                onClearSearchText()
                                viewModel.onSearchTextChanged("")
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

        LaunchedEffect(toastMessage) {
            toastMessage?.getContentIfNotHandled()?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.clearToastMessage()
            }
        }

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
                Scaffold(
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .background(
                                    color = Color.Transparent
                                )
                        ) {
                            CountVacancies(
                                screenState = screenState,
                                vacancies = vacancies,
                                textMessage = stringResource(R.string.found_count_vacancies, state.foundCount)
                            )
                        }
                    },
                    content = { innerPadding ->
                        VacancyListItem(
                            vacancies = state.vacancies,
                            onItemClick = { vacancyId ->
                                navController.navigate(Routes.createVacancyDetailsRoute(vacancyId))
                            },
                            lazyListState = lazyListState,
                            isLoadingNextPage = isLoadingNextPage,
                            hasMorePages = hasMorePages,
                            onRetryClick = { viewModel.retryLastFailedPage() },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                )
            }

            is SearchScreenState.Error.NoConnection -> {
                Placeholder(
                    imageResId = R.drawable.placeholder_no_connection,
                    title = stringResource(R.string.no_connection),
                    modifier = Modifier.padding(top = 135.dp)
                )
            }

            is SearchScreenState.Error.ServerError -> {
                Placeholder(
                    imageResId = R.drawable.placeholder_server_error,
                    title = stringResource(R.string.server_error),
                    modifier = Modifier.padding(top = 135.dp)
                )
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
                        textMessage = stringResource(R.string.empty_search_vacancies)
                    )
                }
                Placeholder(
                    imageResId = R.drawable.placeholder_error_riecive,
                    title = stringResource(R.string.not_found),
                    modifier = Modifier.padding(top = 111.dp)
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
            .fillMaxSize()
            .padding(horizontal = 8.dp),
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
    onItemClick: (String) -> Unit,
    lazyListState: LazyListState,
    isLoadingNextPage: Boolean,
    hasMorePages: Boolean,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(VacancyTheme.colorScheme.background),
    ) {
        items(vacancies) { uiVacancy ->
            VacancyRow(
                vacancyUiModel = uiVacancy,
                onClick = { onItemClick(uiVacancy.vacancy.id) }
            )
        }

        if (isLoadingNextPage) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
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
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        val logoUrl = vacancyUiModel.vacancy.company.logoUrl
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(logoUrl)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.placeholder_logo),
            modifier = Modifier
                .size(48.dp)
                .border(
                    shape = VacancyTheme.shapes.shape12dp,
                    border = BorderStroke(1.dp, VacancyTheme.colorScheme.secondaryContainer)
                )
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
            SalaryDisplay(
                salaryRange = vacancyUiModel.vacancy.salary,
                textStyle = VacancyTheme.typography.regular16,
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
            .padding(top = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = VacancyTheme.colorScheme.primary,
                    shape = VacancyTheme.shapes.shape12dp
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = textMessage,
                color = VacancyTheme.colorScheme.outlineVariant,
                style = VacancyTheme.typography.regular16,
            )
        }
    }
}
