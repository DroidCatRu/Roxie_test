package ru.droidcat.feature_taxi_impl.presentation.rides_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.droidcat.core_navigation.LocalContentPaddings
import ru.droidcat.feature_taxi_impl.R
import ru.droidcat.feature_taxi_impl.presentation.components.Destinations
import ru.droidcat.feature_taxi_impl.presentation.components.LoadingScreen

@Composable
fun RidesListScreen(
    viewModel: RidesListViewModel = hiltViewModel()
) {

    val contentPaddings = LocalContentPaddings.current
    val state = viewModel.screenState.collectAsState()
    val orders = state.value.orders

    if (state.value.ordersLoading && orders == null) {
        LoadingScreen()
    } else {

        if (orders == null) {
            OrdersNotLoaded(
                onTryAgainClick = { viewModel.getOrders() }
            )
        } else {
            SwipeRefresh(
                modifier = Modifier.padding(
                    top = contentPaddings.calculateTopPadding()
                ),
                state = rememberSwipeRefreshState(state.value.ordersLoading),
                onRefresh = { viewModel.getOrders() },
                indicator = { s, trigger ->
                    SwipeRefreshIndicator(
                        state = s,
                        refreshTriggerDistance = trigger,
                        scale = true,
                        fade = true,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
                        end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
                        bottom = contentPaddings.calculateBottomPadding() + 16.dp
                    )
                ) {
                    itemsIndexed(
                        items = orders
                    ) { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onOrderClick(item.id)
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 12.dp,
                                        start = 16.dp,
                                        end = 8.dp
                                    ),
                                verticalArrangement = spacedBy(8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(
                                        R.string.ride_details_date,
                                        item.orderDate
                                    ),
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.ride_details_price, item.price),
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Destinations(
                                    modifier = Modifier.fillMaxWidth(),
                                    startAddress = item.startAddress,
                                    endAddress = item.endAddress
                                )

                                if (index != orders.lastIndex) {
                                    Divider(
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                } else {
                                    Spacer(Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersNotLoaded(
    onTryAgainClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.error_cant_load_rides_list),
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = {
                    onTryAgainClick()
                }
            ) {
                Text(stringResource(R.string.try_again_button_text))
            }
        }
    }
}