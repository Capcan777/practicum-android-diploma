package ru.practicum.android.diploma.navigation.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.navigation.Routes

sealed class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val iconResId: Int
) {
    object Main : BottomNavItem(
        route = Routes.Search.route,
        title = R.string.main_title,
        iconResId = R.drawable.main_24px
    )

    object Favourites : BottomNavItem(
        route = Routes.Favourites.route,
        title = R.string.favourites_title,
        iconResId = R.drawable.fav
    )

    object Team : BottomNavItem(
        route = Routes.About.route,
        title = R.string.team_title,
        iconResId = R.drawable.team_24px
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Main,
    BottomNavItem.Favourites,
    BottomNavItem.Team
)
