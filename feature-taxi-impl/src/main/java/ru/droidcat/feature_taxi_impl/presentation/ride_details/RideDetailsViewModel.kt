package ru.droidcat.feature_taxi_impl.presentation.ride_details

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_taxi_api.model.OrderFull
import ru.droidcat.feature_taxi_api.usecase.GetOrderDetailsUseCase
import ru.droidcat.feature_taxi_api.usecase.LoadImageUseCase
import javax.inject.Inject

@HiltViewModel
class RideDetailsViewModel @Inject constructor(
    @IoScope private val scope: CoroutineScope,
    private val getOrderDetails: GetOrderDetailsUseCase,
    private val loadCarImage: LoadImageUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(RideDetailsScreenState())
    val screenState: StateFlow<RideDetailsScreenState> = _screenState

    private var getDetailsJob: Job? = null
    private var getImageJob: Job? = null

    override fun onCleared() {
        getDetailsJob?.cancel()
        getImageJob?.cancel()
        super.onCleared()
    }

    fun loadDetails(orderId: Long) {
        getDetailsJob?.cancel()
        getDetailsJob = scope.launch {
            _screenState.value = _screenState.value.copy(detailsLoading = true)
            val order = getOrderDetails(orderId)
            _screenState.value = _screenState.value.copy(
                details = order,
                detailsLoading = false
            )
            if (order != null) {
                loadImage()
            }
        }
    }

    fun loadImage() {
        getImageJob?.cancel()
        getImageJob = scope.launch {
            _screenState.value = _screenState.value.copy(carImageLoading = true)
            val order = _screenState.value.details
            if (order != null) {
                _screenState.value = _screenState.value.copy(
                    carImage = loadCarImage(order.carPhoto),
                    carImageLoading = false
                )
            }
        }
    }

}

data class RideDetailsScreenState(
    val details: OrderFull? = null,
    val detailsLoading: Boolean = false,
    val carImage: Bitmap? = null,
    val carImageLoading: Boolean = false
)