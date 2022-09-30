package ru.droidcat.roxie_test

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_taxi_impl.TaxiNavigationDestination

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    factories: Set<NavigationFactory>
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TaxiNavigationDestination.Root.route
    ) {
        factories.forEach {
            it.create(this)
        }
    }
}