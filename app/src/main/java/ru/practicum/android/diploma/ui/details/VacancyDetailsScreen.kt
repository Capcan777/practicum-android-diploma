package ru.practicum.android.diploma.ui.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.domain.api.ExternalNavigator
import ru.practicum.android.diploma.ui.common.AppBar
import ru.practicum.android.diploma.ui.common.Placeholder
import ru.practicum.android.diploma.ui.details.state.VacancyDetailsScreenState
import ru.practicum.android.diploma.util.SalaryDisplay

private const val FAVOURITE_SCALE = 1.12f
private const val DEFAULT_SCALE = 1f

@Composable
fun VacancyDetailsScreen(
    vacancyId: String,
    navController: NavController,
    onBack: () -> Unit,
    viewModel: VacancyDetailsViewModel = koinViewModel { parametersOf(vacancyId) }
) {
    val screenState by viewModel.screenState.collectAsState()
    val isFavourite by viewModel.isFavourite.collectAsState()
    val externalNavigator: ExternalNavigator = koinInject()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.vacancy),
                onBack = onBack,
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.handleShare()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share_vacancy),
                            tint = VacancyTheme.colorScheme.inverseSurface
                        )
                    }
                    FavoriteButton(isFavourite = isFavourite) {
                        if (screenState is VacancyDetailsScreenState.Content) viewModel.toggleFavorite()
                    }
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        when (screenState) {
            is VacancyDetailsScreenState.Loading -> {
                CircularProgressIndicator()
            }

            is VacancyDetailsScreenState.Content -> {
                val vacancy = (screenState as VacancyDetailsScreenState.Content).vacancy

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(VacancyTheme.colorScheme.background)
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Text(
                        text = vacancy.title,
                        style = VacancyTheme.typography.bold32,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    SalaryDisplay(
                        salaryRange = vacancy.salary,
                        textStyle = VacancyTheme.typography.medium22,
                        textColor = VacancyTheme.colorScheme.onPrimaryContainer,
                    )

                    Surface(
                        color = VacancyTheme.colorScheme.secondaryContainer,
                        shape = VacancyTheme.shapes.shape10dp,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CompanyLogo(logoUrl = vacancy.company.logoUrl)

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = vacancy.industry.name,
                                    style = VacancyTheme.typography.medium22,
                                    color = VacancyTheme.colorScheme.onPrimaryContainer
                                )

                                if (vacancy.location != null) {
                                    Text(
                                        text = vacancy.location,
                                        style = VacancyTheme.typography.regular16,
                                        color = VacancyTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }

                    if (vacancy.experience?.name != null) {
                        Text(
                            text = stringResource(R.string.required_experience),
                            style = VacancyTheme.typography.medium16
                        )
                        Text(
                            text = vacancy.experience.name,
                            style = VacancyTheme.typography.regular16,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(
                        text = "${vacancy.employment?.name}. ${vacancy.schedule?.name}",
                        style = VacancyTheme.typography.regular16
                    )
                    Spacer(modifier = Modifier.padding(top = 32.dp))

                    Text(
                        text = stringResource(R.string.vacancy_desc),
                        style = VacancyTheme.typography.medium22,
                    )

                    Text(
                        text = vacancy.description,
                        style = VacancyTheme.typography.regular16,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    if (vacancy.skills.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(top = 24.dp))

                        Text(
                            text = stringResource(R.string.key_skills),
                            style = VacancyTheme.typography.medium22,
                        )
                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Column {
                            vacancy.skills.forEach { skill ->
                                BulletsRow(text = skill)
                            }
                        }
                    }
                    val contacts = vacancy.contacts
                    contacts?.let { contact ->
                        if (contact.name?.isNotBlank() == true ||
                            contact.email?.isNotBlank() == true ||
                            contact.phones?.isNotEmpty() == true
                        ) {
                            Spacer(modifier = Modifier.padding(top = 32.dp))

                            Text(
                                text = stringResource(R.string.contacts),
                                style = VacancyTheme.typography.medium22,
                            )
                            Spacer(modifier = Modifier.padding(top = 16.dp))

                            Column {
                                contacts.name?.takeIf { it.isNotBlank() }?.let { name ->
                                    Text(
                                        text = name,
                                        style = VacancyTheme.typography.regular16,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                contacts.phones?.forEach { phone ->
                                    if (phone.isNotBlank()) {
                                        Text(
                                            text = phone,
                                            style = VacancyTheme.typography.regular16,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                                contacts.email?.takeIf { it.isNotBlank() }?.let { email ->
                                    Text(
                                        text = email,
                                        style = VacancyTheme.typography.regular16,
                                        modifier = Modifier
                                            .padding(bottom = 4.dp)
                                            .clickable(onClick = { externalNavigator.sendEmail(vacancy) }),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is VacancyDetailsScreenState.Error -> {
                when (screenState) {
                    is VacancyDetailsScreenState.Error.ServerError -> {
                        Placeholder(
                            imageResId = R.drawable.placeholder_vacancy_server_error,
                            title = stringResource(R.string.server_error),
                            modifier = Modifier.padding(top = 207.dp)
                        )
                    }

                    is VacancyDetailsScreenState.Error.NotFound -> {
                        Placeholder(
                            imageResId = R.drawable.placeholder_vacancy_delete_or_not_found,
                            title = stringResource(R.string.vacancy_delete_or_not_found),
                            modifier = Modifier.padding(187.dp)
                        )
                    }

                    else -> {
                        // добавить плейсхолдер
                    }
                }
            }
        }
    }
}

@Composable
fun CompanyLogo(logoUrl: String?) {
    // временная заглушка для предпросмотра
    if (LocalInspectionMode.current) {
        Image(
            painter = painterResource(id = R.drawable.placeholder_logo),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(VacancyTheme.shapes.shape12dp),
            contentScale = ContentScale.Crop
        )
    } else {
        val imageLoader: ImageLoader = koinInject()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(logoUrl).build(),
            imageLoader = imageLoader,
            placeholder = painterResource(id = R.drawable.placeholder_logo),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(VacancyTheme.shapes.shape12dp),
            contentScale = ContentScale.Inside
        )
    }
}

@Composable
private fun FavoriteButton(
    isFavourite: Boolean,
    onToggle: () -> Unit
) {
    val inactiveColor = VacancyTheme.colorScheme.onSurface
    val targetColor = if (isFavourite) VacancyTheme.colorScheme.error else inactiveColor
    val animatedTint by animateColorAsState(targetColor)
    val scale by animateFloatAsState(if (isFavourite) FAVOURITE_SCALE else DEFAULT_SCALE)

    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = stringResource(R.string.add_vacancy_to_favourite),
            tint = animatedTint,
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun BulletsRow(
    text: String
) {
    Row {
        Text(
            text = "•",
            style = VacancyTheme.typography.regular16,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = VacancyTheme.typography.regular16,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}
