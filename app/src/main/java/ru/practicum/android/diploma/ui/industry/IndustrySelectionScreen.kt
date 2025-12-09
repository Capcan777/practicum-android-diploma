package ru.practicum.android.diploma.ui.industry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.common.AppBar
import ru.practicum.android.diploma.ui.industry.state.IndustrySelectionState

@Composable
fun IndustrySelectionScreen(
    navController: NavController,
    onBack: () -> Unit,
    onIndustrySelected: (Industry) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner? = null
) {
    val viewModel: IndustrySelectionViewModel = if (viewModelStoreOwner != null) {
        koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
    } else {
        koinViewModel()
    }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.select_industry),
                onBack = onBack,
                actions = null
            )
        }
    ) { innerPadding ->
        IndustrySelectionContent(
            uiState = uiState,
            searchQuery = uiState.searchQuery,
            onSearchTextChanged = viewModel::onSearchTextChanged,
            onClearSearch = viewModel::clearSearch,
            onIndustrySelected = { industry ->
                viewModel.onIndustrySelected(industry)
                onIndustrySelected(industry)
                onBack()
            },
            modifier = Modifier
                .fillMaxSize()
                .background(VacancyTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        )
    }
}

@Composable
private fun IndustrySelectionContent(
    uiState: IndustrySelectionState,
    searchQuery: String,
    onSearchTextChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onIndustrySelected: (Industry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchTextField(
            query = searchQuery,
            onQueryChanged = onSearchTextChanged,
            onClear = onClearSearch
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Text(
                    text = stringResource(R.string.error_failed_to_load_data),
                    style = VacancyTheme.typography.regular16,
                    color = VacancyTheme.colorScheme.error
                )
            }
            uiState.filteredIndustries.isEmpty() -> {
                Text(
                    text = stringResource(R.string.empty_search_vacancies),
                    style = VacancyTheme.typography.regular16,
                    color = VacancyTheme.colorScheme.onBackground
                )
            }
            else -> {
                LazyColumn {
                    items(uiState.filteredIndustries) { industry ->
                        IndustryItem(
                            industry = industry,
                            isSelected = uiState.selectedIndustry?.id == industry.id,
                            onSelect = { onIndustrySelected(industry) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = stringResource(R.string.enter_industry),
                style = VacancyTheme.typography.regular16,
                color = VacancyTheme.colorScheme.secondary
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (query.isEmpty()) Icons.Filled.Search else Icons.Filled.Clear,
                contentDescription = null,
                tint = VacancyTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.clickable {
                    if (query.isNotEmpty()) {
                        onClear()
                    }
                }
            )
        },
        singleLine = true,
        shape = VacancyTheme.shapes.shape10dp,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VacancyTheme.colorScheme.secondaryContainer,
            unfocusedBorderColor = VacancyTheme.colorScheme.secondaryContainer,
            cursorColor = VacancyTheme.colorScheme.primary,
            focusedContainerColor = VacancyTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = VacancyTheme.colorScheme.secondaryContainer,
            unfocusedLabelColor = VacancyTheme.colorScheme.inverseSurface,
            focusedLabelColor = VacancyTheme.colorScheme.primary,
        )
    )
}

@Composable
private fun IndustryItem(
    industry: Industry,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = industry.name,
            style = VacancyTheme.typography.regular16,
            color = VacancyTheme.colorScheme.inverseSurface,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = VacancyTheme.colorScheme.primary,
                unselectedColor = VacancyTheme.colorScheme.primary
            )
        )
    }
}
