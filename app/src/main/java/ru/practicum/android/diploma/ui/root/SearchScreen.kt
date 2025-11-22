package ru.practicum.android.diploma.ui.root

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R

@Composable
fun SearchScreen() {
    Text(text = stringResource(R.string.vacancies_search))
}
