package ru.practicum.android.diploma.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.practicum.android.diploma.designsystem.theme.VacancyTheme

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column {
        HorizontalDivider(
            color = VacancyTheme.colorScheme.outlineVariant,
            thickness = 1.dp
        )
        NavigationBar(
            containerColor = VacancyTheme.colorScheme.surface,
            contentColor = VacancyTheme.colorScheme.onSurface,
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = VacancyTheme.typography.regular12
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VacancyTheme.colorScheme.primary,
                        selectedTextColor = VacancyTheme.colorScheme.primary,
                        unselectedIconColor = VacancyTheme.colorScheme.secondary,
                        unselectedTextColor = VacancyTheme.colorScheme.secondary,
                        indicatorColor = VacancyTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}
