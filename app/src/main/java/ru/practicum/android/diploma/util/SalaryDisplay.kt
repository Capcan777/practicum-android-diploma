package ru.practicum.android.diploma.util

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.domain.models.SalaryRange

@Composable
fun SalaryDisplay(salaryRange: SalaryRange?) {
    if (salaryRange == null) {
        Text(
            text = stringResource(R.string.salary_not_specified),
            style = VacancyTheme.typography.regular16,
            color = VacancyTheme.colorScheme.onPrimaryContainer
        )
        return
    }

    val currencySymbol = formatCurrency(salaryRange.currency)

    val displaySalary = when {
        salaryRange.from != null && salaryRange.to != null ->
            "от ${formatWithSpaces(salaryRange.from)} до ${formatWithSpaces(salaryRange.to)} $currencySymbol"

        salaryRange.from != null ->
            "от ${formatWithSpaces(salaryRange.from)} $currencySymbol"

        salaryRange.to != null ->
            "до ${formatWithSpaces(salaryRange.to)} $currencySymbol"

        else -> stringResource(R.string.salary_not_specified)
    }
    Text(
        text = displaySalary,
        style = VacancyTheme.typography.regular16,
        color = VacancyTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun formatCurrency(currency: String?): String {
    return when (currency) {
        stringResource(R.string.currency_rub) ->
            stringResource(R.string.currency_rub_sym)

        stringResource(R.string.currency_usd),
        stringResource(R.string.currency_aud) ->
            stringResource(R.string.currency_usd_sym)

        stringResource(R.string.currency_eur) ->
            stringResource(R.string.currency_eur_sym)

        null -> stringResource(R.string.salary_not_specified)
        else -> currency
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun formatWithSpaces(value: Int?): String {
    return value?.let {
        String.format("%,d", it).replace(",", " ")
    } ?: ""
}
