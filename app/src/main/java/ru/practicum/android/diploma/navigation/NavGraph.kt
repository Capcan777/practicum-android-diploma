package ru.practicum.android.diploma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.ui.about.AboutTeamScreen
import ru.practicum.android.diploma.ui.details.VacancyDetailsScreen
import ru.practicum.android.diploma.ui.favourites.FavouritesVacanciesScreen
import ru.practicum.android.diploma.ui.filter.FilterSettingsScreen
import ru.practicum.android.diploma.ui.search.SearchScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Search.route) {
        composable(Routes.Search.route) { SearchScreen() }
        composable(Routes.VacancyDetails.route) { backStackEntry ->
            val vacancyId = backStackEntry.arguments?.getString("vacancyId")
            if (vacancyId == null) {
                return@composable
            }
            VacancyDetailsScreen(vacancyId)
        }
        composable(Routes.Favourites.route) { FavouritesVacanciesScreen() }
        composable(Routes.SettingsFilter.route) { FilterSettingsScreen() }
        composable(Routes.About.route) { AboutTeamScreen() }
    }
}


sealed class Routes(val route: String) {
    object Search : Routes("search")
    object VacancyDetails : Routes("vacancy_details/{vacancyId}") {
        fun createRoute(vacancyId: String) = "vacancy_details/$vacancyId"
    }
    object Favourites : Routes("favourites")
    object SettingsFilter : Routes("filter")
    object About : Routes("about")
}
