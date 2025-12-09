package ru.practicum.android.diploma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import ru.practicum.android.diploma.ui.about.AboutTeamScreen
import ru.practicum.android.diploma.ui.details.VacancyDetailsScreen
import ru.practicum.android.diploma.ui.favourites.FavouritesVacanciesScreen
import ru.practicum.android.diploma.ui.filter.FilterSettingsScreen
import ru.practicum.android.diploma.ui.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.ui.industry.IndustrySelectionScreen
import ru.practicum.android.diploma.ui.search.SearchScreen
import ru.practicum.android.diploma.ui.search.SearchViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Search.route) {
        composable(Routes.Search.route) { backStackEntry ->
            SearchScreen(
                navController,
                onClearSearchText = {},
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
        composable(Routes.SettingsFilter.route) { backStackEntry ->
            val previousEntry = navController.previousBackStackEntry
            val searchViewModel: SearchViewModel? = previousEntry?.let {
                koinViewModel<SearchViewModel>(viewModelStoreOwner = it)
            }

            FilterSettingsScreen(
                navController,
                onBack = {
                    searchViewModel?.refreshSearchWithCurrentQuery()
                    navController.popBackStack()
                },
                viewModelStoreOwner = backStackEntry
            )
        }
        composable(Routes.IndustrySelection.route) { backStackEntry ->
            val previousEntry = navController.previousBackStackEntry
            val filterViewModel: FilterSettingsViewModel? = previousEntry?.let {
                koinViewModel<FilterSettingsViewModel>(viewModelStoreOwner = it)
            }

            IndustrySelectionScreen(
                onBack = { navController.popBackStack() },
                onIndustrySelected = { industry ->
                    filterViewModel?.onIndustryChanged(industry.name)
                },
                viewModelStoreOwner = backStackEntry
            )
        }
        composable(Routes.About.route) { AboutTeamScreen() }
    }
}

data class Routes(val route: String) {
    companion object {
        const val VACANCY_DETAILS_BASE = "vacancy_details"
        const val SETTINGS_FILTER_BASE = "filter"
        const val INDUSTRY_SELECTION_BASE = "industry_selection"
        const val SEARCH_BASE = "search"
        const val ABOUT_BASE = "about"
        const val FAVOURITES_BASE = "favourites"

        val Search = Routes(SEARCH_BASE)
        val VacancyDetails = Routes("$VACANCY_DETAILS_BASE/{vacancyId}")
        val Favourites = Routes(FAVOURITES_BASE)
        val SettingsFilter = Routes(SETTINGS_FILTER_BASE)
        val IndustrySelection = Routes(INDUSTRY_SELECTION_BASE)
        val About = Routes(ABOUT_BASE)

        fun createVacancyDetailsRoute(vacancyId: String) = "vacancy_details/$vacancyId"
    }
}
