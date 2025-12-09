package ru.practicum.android.diploma.util

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.domain.models.SalaryRange
import java.util.Locale

@Composable
fun SalaryDisplay(
    salaryRange: SalaryRange?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    textColor: Color
) {
    val context = LocalContext.current
    val resourceProvider = remember { ResourceProvider(context) }

    if (salaryRange == null) {
        Text(
            text = stringResource(R.string.salary_not_specified),
            style = VacancyTheme.typography.regular16,
            color = VacancyTheme.colorScheme.onPrimaryContainer
        )
        return
    }

    val symbol = formatCurrency(resourceProvider, salaryRange.currency)
    val salaryFrom = salaryRange.from?.let { "${formatWithSpaces(it)}${if (symbol.isNotBlank()) " $symbol" else ""}" }
    val salaryTo = salaryRange.to?.let { "${formatWithSpaces(it)}${if (symbol.isNotBlank()) " $symbol" else ""}" }

    val displaySalary = when {
        salaryFrom != null && salaryTo != null ->
            resourceProvider.getString(R.string.salary_range, salaryFrom, salaryTo)

        salaryFrom != null ->
            resourceProvider.getString(R.string.salary_from, salaryFrom)

        salaryTo != null ->
            resourceProvider.getString(R.string.salary_to, salaryTo)

        else ->
            resourceProvider.getString(R.string.salary_not_specified)
    }
    Text(
        text = displaySalary,
        style = textStyle,
        color = textColor,
        modifier = modifier
    )
}

fun formatCurrency(resourceProvider: ResourceProvider, currency: String?): String {
    return when (currency) {
        resourceProvider.getString(R.string.currency_rub) ->
            resourceProvider.getString(R.string.currency_rub_sym)

        resourceProvider.getString(R.string.currency_usd),
        resourceProvider.getString(R.string.currency_aud),
        resourceProvider.getString(R.string.currency_hkd) ->
            resourceProvider.getString(R.string.currency_usd_sym)

        resourceProvider.getString(R.string.currency_eur) ->
            resourceProvider.getString(R.string.currency_eur_sym)

        resourceProvider.getString(R.string.currency_gbp) ->
            resourceProvider.getString(R.string.currency_gbp_sym)

        resourceProvider.getString(R.string.currency_sek) ->
            resourceProvider.getString(R.string.currency_sek_sym)

        null -> ""
        else -> currency
    }
}

fun formatWithSpaces(value: Int?): String {
    return value?.let {
        String.format(Locale.forLanguageTag("ru-RU"), "%,d", it).replace(",", " ")
    } ?: ""
}
