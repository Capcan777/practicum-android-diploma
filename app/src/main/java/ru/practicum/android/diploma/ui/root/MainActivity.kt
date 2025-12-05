package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme
import ru.practicum.android.diploma.navigation.NavGraph
import ru.practicum.android.diploma.navigation.Routes
import ru.practicum.android.diploma.navigation.bottomnavigation.BottomNavigationBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VacancyTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        Scaffold(
            bottomBar = {
                if (currentRoute?.startsWith(Routes.VACANCY_DETAILS_BASE) != true) {
                    BottomNavigationBar(navController = navController)
                }
            }
        ) { innerPadding ->
            Surface(
                color = VacancyTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavGraph(navController = navController)
            }
        }
    }
}
