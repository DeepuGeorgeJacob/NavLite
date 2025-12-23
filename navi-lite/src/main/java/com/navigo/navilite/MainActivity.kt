package com.navigo.navilite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.navigo.navilite.page.navigation.AppNavigation
import com.navigo.navilite.page.navigation.Route
import com.navigo.ui.components.NavLiteTopBar
import com.navigo.ui.theme.NaviLiteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NaviLiteTheme(dynamicColor = false) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val destination = navBackStackEntry?.destination
                val (title, showBack) = destination.getTopBarDetails()

                Scaffold(
                    topBar = {
                        NavLiteTopBar(
                            title = stringResource(title),
                            onBackClick = if (showBack) {
                                { navController.popBackStack() }
                            } else null
                        )
                    }
                ) { paddingValues ->
                    AppNavigation(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController
                    )
                }
            }
        }
    }

    fun NavDestination?.getTopBarDetails(): Pair<Int, Boolean> = when {
        this?.hasRoute<Route.Details>() == true ->
            Pair(R.string.details, true)

        this?.hasRoute<Route.Home>() == true ->
            Pair(R.string.app_name, false)

        else ->
            Pair(R.string.app_name, false)
    }

}
