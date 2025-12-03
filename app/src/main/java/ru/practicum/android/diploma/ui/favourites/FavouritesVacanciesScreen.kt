package ru.practicum.android.diploma.ui.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import ru.practicum.android.diploma.ui.common.Placeholder
import ru.practicum.android.diploma.ui.favourites.state.FavouritesScreenState
import ru.practicum.android.diploma.util.SalaryDisplay


@Composable
fun FavouritesVacanciesScreen(
    navController: NavController,
    viewModel: FavouritesViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VacancyTheme.colorScheme.background)
    ) {
        Text(
            text = stringResource(R.string.favourites),
            style = VacancyTheme.typography.medium22,
            color = VacancyTheme.colorScheme.inverseSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp, vertical = 19.dp)
        )

        when (val state = screenState) {
            is FavouritesScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is FavouritesScreenState.Empty -> {
                Placeholder(
                    imageResId = R.drawable.placeholder_empty_favourites,
                    title = stringResource(R.string.empty_favourites),
                    modifier = Modifier.padding(top = 171.dp)
                )
            }

            is FavouritesScreenState.Error -> {
                Placeholder(
                    imageResId = R.drawable.placeholder_error_riecive,
                    title = stringResource(R.string.not_found),
                    modifier = Modifier.padding(top = 158.dp)
                )
            }

            is FavouritesScreenState.Content -> {
                val vacancies = state.items
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(vacancies, key = { it.id }) { vacancy ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("details/${vacancy.id}") }
                                .padding(vertical = 12.dp)
                        ) {
                            val imageLoader: ImageLoader = koinInject()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                // .clickable { onClick() },
                                verticalAlignment = Alignment.Top
                            ) {
                                val logoUrl = vacancy.company.logoUrl
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(logoUrl)
                                        .build(),
                                    imageLoader = imageLoader,
                                    contentDescription = null,
                                    placeholder = painterResource(id = R.drawable.placeholder_logo),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(VacancyTheme.shapes.shape12dp)
                                        .padding(start = 16.dp),
                                    contentScale = ContentScale.Inside
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(
                                        text = "${vacancy.title}, ${vacancy.location}",
                                        style = VacancyTheme.typography.medium22,
                                        color = VacancyTheme.colorScheme.inverseSurface,
                                        textAlign = TextAlign.Start,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        text = vacancy.company.name,
                                        style = VacancyTheme.typography.regular16,
                                        color = VacancyTheme.colorScheme.inverseSurface

                                    )
                                    SalaryDisplay(
                                        vacancy.salary,
                                        textStyle = VacancyTheme.typography.regular16,
                                        textColor = VacancyTheme.colorScheme.onPrimaryContainer,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
