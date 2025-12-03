package ru.practicum.android.diploma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.practicum.android.diploma.ui.about.AboutTeamScreen
import ru.practicum.android.diploma.ui.details.VacancyDetailsScreen
import ru.practicum.android.diploma.ui.favourites.FavouritesVacanciesScreen
import ru.practicum.android.diploma.ui.filter.FilterSettingsScreen
import ru.practicum.android.diploma.ui.search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Search.route) {
        composable(Routes.Search.route) { backStackEntry ->
            SearchScreen(
                navController,
                onClearSearchText = {}, // добавить функцию очистки поля поиска
                viewModelStoreOwner = backStackEntry
            )
        }
        composable(Routes.VacancyDetails.route) { backStackEntry ->
            val vacancyId = backStackEntry.arguments?.getString("vacancyId")
            if (vacancyId != null) {
                VacancyDetailsScreen(
                    vacancyId = vacancyId,
                    navController = navController,
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Обработать ошибку
            }
        }
        composable(Routes.Favourites.route) { FavouritesVacanciesScreen(navController) }
        composable(Routes.SettingsFilter.route) { FilterSettingsScreen(navController) }
        composable(Routes.About.route) { AboutTeamScreen(navController) }
    }
}

data class Routes(val route: String) {
    companion object {
        const val VACANCY_DETAILS_BASE = "vacancy_details"
        val Search = Routes("search")
        val VacancyDetails = Routes("$VACANCY_DETAILS_BASE/{vacancyId}")
        val Favourites = Routes("favourites")
        val SettingsFilter = Routes("filter")
        val About = Routes("about")

        // Функция для создания route с параметрами
        fun createVacancyDetailsRoute(vacancyId: String) = "vacancy_details/$vacancyId"
    }
}
