package ru.practicum.android.diploma.ui.filter

import android.util.Log
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.ui.common.AppBar

@Composable
fun FilterSettingsScreen(
    navController: NavController,
    onBack: () -> Unit
) {
    var salaryText by remember { mutableStateOf("") }
    var hideWithoutSalary by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.filter_settings),
                onBack = onBack,
                actions = null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VacancyTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            FilterRow(
                text = stringResource(R.string.place_of_work),
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
                    onClick = { } // обработать клик на страницу отрасли
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = salaryText,
                onValueChange = {
                    if (salaryText.all { it.isDigit() }) {
                        salaryText = it
                    }
                },
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
                    if (salaryText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                            tint = VacancyTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.clickable {
                                salaryText = ""
                                // обработка нажатия на кнопку очистки
                            }
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { hideWithoutSalary = !hideWithoutSalary },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.do_not_show_without_salary),
                    style = VacancyTheme.typography.regular16,
                )
                Checkbox(
                    checked = hideWithoutSalary,
                    onCheckedChange = { hideWithoutSalary = it },
                    colors = CheckboxDefaults.colors(
                        VacancyTheme.colorScheme.primary,
                        uncheckedColor = VacancyTheme.colorScheme.primary
                    )

                )
            }
            Spacer(modifier = Modifier.weight(1f))

            ButtonApply(
                onClick = { /* обработать нажатие на кнопку Применить */ },
                textButton = "Применить",
                color = ButtonDefaults.buttonColors(
                    containerColor = VacancyTheme.colorScheme.primary,
                    contentColor = VacancyTheme.colorScheme.onPrimary
                ),
                colorText = VacancyTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            ButtonApply(
                onClick = { /* обработать нажатие на кнопку Сбросить */ },
                textButton = stringResource(R.string.reset),
                color = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = VacancyTheme.colorScheme.error
                ),
                colorText = VacancyTheme.colorScheme.error
            )
        }
    }
    }

@Composable
private fun FilterRow(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = VacancyTheme.typography.regular16,
            color = VacancyTheme.colorScheme.onBackground
        )
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
