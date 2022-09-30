package ru.droidcat.feature_taxi_impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_taxi_impl.presentation.ride_details.RideDetailsScreen
import ru.droidcat.feature_taxi_impl.presentation.rides_list.RidesListScreen
import javax.inject.Inject

class TaxiNavigationFactory @Inject constructor() : NavigationFactory {
    override fun create(builder: NavGraphBuilder) {
        builder.navigation(
            route = TaxiNavigationDestination.Root.route,
            startDestination = TaxiNavigationDestination.RidesList.route
        ) {
            composable(
                route = TaxiNavigationDestination.RidesList.route
            ) {
                RidesListScreen()
            }

            composable(
                route = "${TaxiNavigationDestination.RideDetails.route}/{orderId}",
                arguments = listOf(
                    navArgument("orderId") {
                        type = NavType.LongType
                    }
                )
            ) {
                RideDetailsScreen(it.arguments?.getLong("orderId"))
            }
        }
    }
}

sealed class TaxiNavigationDestination(val route: String) {
    object Root : TaxiNavigationDestination("taxi_root")
    object RidesList : TaxiNavigationDestination("rides_screen")
    object RideDetails : TaxiNavigationDestination("ride_details")
}