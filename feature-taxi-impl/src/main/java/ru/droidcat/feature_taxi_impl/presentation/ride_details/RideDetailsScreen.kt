package ru.droidcat.feature_taxi_impl.presentation.ride_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.droidcat.core_navigation.LocalContentPaddings
import ru.droidcat.feature_taxi_impl.R
import ru.droidcat.feature_taxi_impl.presentation.components.CarImage
import ru.droidcat.feature_taxi_impl.presentation.components.CarNumber
import ru.droidcat.feature_taxi_impl.presentation.components.Destinations
import ru.droidcat.feature_taxi_impl.presentation.components.LoadingScreen

@Composable
fun RideDetailsScreen(
    orderId: Long?,
    viewModel: RideDetailsViewModel = hiltViewModel()
) {

    val contentPaddings = LocalContentPaddings.current
    val state = viewModel.screenState.collectAsState()

    if (orderId == null) {
        OrderNotChosen()
    } else {

        LaunchedEffect(Unit) {
            viewModel.loadDetails(orderId)
        }

        if (state.value.detailsLoading) {
            LoadingScreen()
        } else {

            val order = state.value.details

            if (order == null) {
                OrderNotLoaded(
                    id = orderId,
                    onTryAgainClick = { viewModel.loadDetails(orderId) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(
                        start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                        top = contentPaddings.calculateTopPadding() + 16.dp,
                        end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current) + 16.dp,
                        bottom = contentPaddings.calculateBottomPadding() + 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = spacedBy(16.dp)
                ) {

                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.ride_details_date, order.orderDate),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.weight(1.0f),
                                text = stringResource(R.string.ride_details_time, order.orderTime),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = order.price,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    item {
                        Divider()
                    }

                    item {
                        Destinations(
                            modifier = Modifier.fillMaxWidth(),
                            startAddress = order.startAddress,
                            endAddress = order.endAddress
                        )
                    }

                    item {
                        Divider()
                    }

                    item {
                        CarImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f),
                            image = state.value.carImage,
                            imageLoading = state.value.carImageLoading,
                            loadImage = { viewModel.loadImage() }
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1.0f)
                            ) {
                                Text(
                                    text = stringResource(R.string.ride_details_vehicle),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = order.carModel,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            CarNumber(
                                number = order.carNumber,
                                region = order.carRegion
                            )
                        }
                    }

                    item {
                        Divider()
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.ride_details_driver),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = order.driverName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderNotChosen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.error_order_not_chosen),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun OrderNotLoaded(
    id: Long,
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
                text = stringResource(R.string.error_cant_load_ride_details, id),
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