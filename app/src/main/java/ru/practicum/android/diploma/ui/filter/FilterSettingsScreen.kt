package ru.practicum.android.diploma.ui.filter

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.ui.common.AppBar


@Composable
fun FilterSettingsScreen(
    navController: NavController,
    onBack: () -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner? = null
) {
    val viewModel: FilterSettingsViewModel = if (viewModelStoreOwner != null) {
        koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
    } else {
        koinViewModel()
    }
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.filter_settings),
                onBack = onBack,
                actions = null
            )
        }
    ) { innerPadding ->
        FilterSettingsContent(
            uiState = uiState.value,
            onSalaryChanged = viewModel::onSalaryChanged,
            onClearSalary = viewModel::clearSalary,
            onCheckboxChanged = viewModel::onCheckboxChanged,
            onApplyFilters = { /* обрабока нажатия для применения фильтров viewModel.applyFilters() */ },
            onResetFilters = viewModel::resetFilters,
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
private fun FilterSettingsContent(
    uiState: ru.practicum.android.diploma.ui.filter.state.FilterSettingsState,
    onSalaryChanged: (String) -> Unit,
    onClearSalary: () -> Unit,
    onCheckboxChanged: (Boolean) -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FilterRow(
            text = stringResource(R.string.place_of_work),
            selectedText = uiState.placeOfWork,
            onClick = { } // без клика
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            FilterRow(
                text = stringResource(R.string.industry),
                selectedText = uiState.industry,
                onClick = { } // обработать клик на страницу отрасли
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SalaryTextField(
            salary = uiState.salary,
            onSalaryChanged = onSalaryChanged,
            onClearSalary = onClearSalary
        )

        Spacer(modifier = Modifier.height(16.dp))

        HideWithoutSalaryCheckbox(
            isChecked = uiState.hideWithoutSalary,
            onCheckedChange = onCheckboxChanged
        )

        Spacer(modifier = Modifier.weight(1f))

        if (uiState.isFilterApplied) {
            FilterActionButtons(
                onApplyFilters = onApplyFilters,
                onResetFilters = onResetFilters
            )
        }
    }
}

@Composable
private fun SalaryTextField(
    salary: String,
    onSalaryChanged: (String) -> Unit,
    onClearSalary: () -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = salary,
        onValueChange = onSalaryChanged,
        label = {
            Text(
                text = stringResource(R.string.expected_salary),
                style = VacancyTheme.typography.regular12,
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.enter_amount),
                style = VacancyTheme.typography.regular16,
                color = VacancyTheme.colorScheme.secondary
            )
        },
        trailingIcon = {
            if (salary.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    tint = VacancyTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clickable(onClick = onClearSalary)
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        shape = VacancyTheme.shapes.shape10dp,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VacancyTheme.colorScheme.tertiaryContainer,
            unfocusedBorderColor = VacancyTheme.colorScheme.tertiaryContainer,
            cursorColor = VacancyTheme.colorScheme.primary,
            focusedContainerColor = VacancyTheme.colorScheme.tertiaryContainer,
            unfocusedContainerColor = VacancyTheme.colorScheme.tertiaryContainer,
            unfocusedLabelColor = VacancyTheme.colorScheme.inverseSurface,
            focusedLabelColor = VacancyTheme.colorScheme.primary,
        )
    )
}

@Composable
private fun HideWithoutSalaryCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.do_not_show_without_salary),
            style = VacancyTheme.typography.regular16,
            modifier = Modifier
                .weight(1f)
                .clickable { onCheckedChange(!isChecked) }
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = VacancyTheme.colorScheme.primary,
                uncheckedColor = VacancyTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun FilterActionButtons(
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit
) {
    Column {
        ButtonApply(
            onClick = onApplyFilters,
            textButton = stringResource(R.string.apply),
            color = ButtonDefaults.buttonColors(
                containerColor = VacancyTheme.colorScheme.primary,
                contentColor = VacancyTheme.colorScheme.onPrimary
            ),
            colorText = VacancyTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        ButtonApply(
            onClick = onResetFilters,
            textButton = stringResource(R.string.reset),
            color = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = VacancyTheme.colorScheme.error
            ),
            colorText = VacancyTheme.colorScheme.error
        )
    }
}

@Composable
private fun FilterRow(
    text: String,
    selectedText: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .heightIn(min = 60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                style = VacancyTheme.typography.regular16,
                color = VacancyTheme.colorScheme.onBackground
            )
            // Показываем выбранное значение под основным текстом, если оно есть
            if (!selectedText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = selectedText,
                    style = VacancyTheme.typography.regular16,
                    color = VacancyTheme.colorScheme.onBackground
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = VacancyTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ButtonApply(
    onClick: () -> Unit,
    textButton: String,
    color: ButtonColors,
    colorText: Color
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = VacancyTheme.shapes.shape12dp,
        colors = color,
        content = {
            Text(
                text = textButton,
                style = VacancyTheme.typography.regular16,
                color = colorText
            )
        },
        contentPadding = PaddingValues(vertical = 20.dp),
    )
}
