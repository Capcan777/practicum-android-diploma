package ru.practicum.android.diploma.ui.filter

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R

@Composable
fun FilterSettingsScreen(
    navController: NavController
) {
    Text(text = stringResource(R.string.filter_settings))
}
