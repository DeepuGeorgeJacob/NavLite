package com.navigo.navilite.page.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.navigo.navilite.feature.details.presentation.DetailsScreen
import com.navigo.navilite.feature.home.domain.model.Id
import com.navigo.navilite.feature.home.presentation.HomeScreen
import kotlinx.serialization.Serializable


@Serializable
internal sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data class Details(val placeId: Int) : Route


}

@Composable
internal fun AppNavigation(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    val onNavigateToDetails = remember(navController) {
        { placeId: Id ->
            navController.navigate(Route.Details(placeId.value))
        }
    }
    NavHost(navController = navController, startDestination = Route.Home, modifier = modifier) {
        composable<Route.Home> {
            HomeScreen(onNavigateToDetails = onNavigateToDetails)
        }
        composable<Route.Details> {
            DetailsScreen()
        }
    }
}