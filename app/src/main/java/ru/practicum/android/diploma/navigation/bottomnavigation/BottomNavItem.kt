package ru.practicum.android.diploma.navigation.bottomnavigation

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.navigation.Routes

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val iconResId: Int
) {
    object Main : BottomNavItem(
        route = Routes.Search.route,
        title = "Главная",
        iconResId = R.drawable.main_24px
    )

    object Favourites : BottomNavItem(
        route = Routes.Favourites.route,
        title = "Избранное",
        iconResId = R.drawable.fav
    )

    object Team : BottomNavItem(
        route = Routes.About.route,
        title = "Команда",
        iconResId = R.drawable.team_24px
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Main,
    BottomNavItem.Favourites,
    BottomNavItem.Team
)
