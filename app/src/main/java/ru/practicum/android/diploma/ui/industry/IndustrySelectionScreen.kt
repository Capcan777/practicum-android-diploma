package ru.practicum.android.diploma.ui.industry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.common.AppBar
import ru.practicum.android.diploma.ui.common.Placeholder
import ru.practicum.android.diploma.ui.industry.state.IndustrySelectionState

@Composable
fun IndustrySelectionScreen(
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
            onIndustrySelected = viewModel::onIndustrySelected,
            onSelectButtonClick = {
                viewModel.saveSelectedIndustry {
                    onBack()
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(VacancyTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(start = 16.dp)
                .padding(top = 8.dp)
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
    onSelectButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SearchTextField(
            query = searchQuery,
            onQueryChanged = onSearchTextChanged,
            onClear = onClearSearch
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                Placeholder(
                    imageResId = R.drawable.placeholder_no_connection,
                    title = stringResource(R.string.error_no_internet_connection),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 122.dp)
                )
            }

            uiState.filteredIndustries.isEmpty() -> {
                Placeholder(
                    imageResId = R.drawable.placeholder_error_recieve_industries,
                    title = stringResource(R.string.no_industries_found),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 122.dp)
                )
            }

            else -> {
                LazyColumn(Modifier.weight(1f)) {
                    val sortedIndustries = uiState.filteredIndustries.sortedBy { it.name }

                    items(sortedIndustries) { industry ->
                        IndustryItem(
                            industry = industry,
                            isSelected = uiState.selectedIndustry?.id == industry.id,
                            onSelect = { onIndustrySelected(industry) }
                        )
                    }

                }
            }
        }

        Spacer(modifier = Modifier)

        if (uiState.selectedIndustry != null && uiState.isUserSelected) {
            Button(
                onClick = onSelectButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, end = 16.dp),
                shape = VacancyTheme.shapes.shape12dp,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VacancyTheme.colorScheme.primary,
                    contentColor = VacancyTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.select),
                    style = VacancyTheme.typography.medium16,
                    color = VacancyTheme.colorScheme.outlineVariant
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = stringResource(R.string.enter_industry),
                style = VacancyTheme.typography.regular16,
                color = VacancyTheme.colorScheme.onBackground
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (query.isEmpty()) Icons.Filled.Search else Icons.Filled.Clear,
                contentDescription = null,
                tint = VacancyTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clickable {
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
            .padding(vertical = 12.dp)
            .padding(end = 2.dp),
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
