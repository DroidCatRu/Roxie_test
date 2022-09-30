package ru.droidcat.feature_taxi_impl.presentation.rides_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.droidcat.core_navigation.FeatureIntentManager
import ru.droidcat.core_utils.IoScope
import ru.droidcat.feature_taxi_api.intent.TaxiOrderChosen
import ru.droidcat.feature_taxi_api.model.OrderBasic
import ru.droidcat.feature_taxi_api.usecase.LoadActiveOrdersUseCase
import javax.inject.Inject

@HiltViewModel
class RidesListViewModel @Inject constructor(
    @IoScope private val scope: CoroutineScope,
    private val featureIntentManager: FeatureIntentManager,
    private val loadActiveOrdersUseCase: LoadActiveOrdersUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(RidesListScreenState())
    val screenState: StateFlow<RidesListScreenState> = _screenState

    private var getOrdersJob: Job? = null

    init {
        getOrders()
    }

    override fun onCleared() {
        getOrdersJob?.cancel()
        super.onCleared()
    }

    fun onOrderClick(id: Long) {
        featureIntentManager.sendIntent(TaxiOrderChosen(id))
    }

    fun getOrders() {
        getOrdersJob?.cancel()
        getOrdersJob = scope.launch {
            _screenState.value = _screenState.value.copy(ordersLoading = true)
            _screenState.value = _screenState.value.copy(
                orders = loadActiveOrdersUseCase(),
                ordersLoading = false
            )
        }
    }
}

data class RidesListScreenState(
    val orders: List<OrderBasic>? = null,
    val ordersLoading: Boolean = false
)