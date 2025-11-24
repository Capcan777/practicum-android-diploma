package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.designSystem.uiKit.VacancyTheme
import ru.practicum.android.diploma.navigation.NavGraph

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacancyTheme(isDarkTheme = false) {
                RootScreen()
                NavGraph()
            }
        }
    }

    private fun networkRequestExample(accessToken: String) {
    }

    @Composable
    fun RootScreen() {
        Surface(
            color = VacancyTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Добро пожаловать в Diploma App!",
                style = VacancyTheme.typography.bold32,
                color = VacancyTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
