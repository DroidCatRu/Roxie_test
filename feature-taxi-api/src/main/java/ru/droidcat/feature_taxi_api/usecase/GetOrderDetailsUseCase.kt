package ru.droidcat.feature_taxi_api.usecase

import ru.droidcat.core_remote_api.LoadResult
import ru.droidcat.core_remote_api.RemoteRepository
import ru.droidcat.feature_taxi_api.model.OrderFull
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetOrderDetailsUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend operator fun invoke(orderId: Long): OrderFull? {
        try {
            when (val loadResult = remoteRepository.loadActiveOrders()) {
                is LoadResult.ERROR -> {
                    return null
                }
                is LoadResult.SUCCESS -> {
                    val order = loadResult.data.find {
                        it.id == orderId
                    } ?: return null

                    val dateTime =
                        ZonedDateTime.parse(order.orderTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                    val date = dateTime.format(
                        DateTimeFormatter
                            .ofPattern("dd.MM.yyyy")
                            .withZone(ZoneId.systemDefault())
                    )
                    val time = dateTime.format(
                        DateTimeFormatter
                            .ofPattern("HH:mm")
                            .withZone(ZoneId.systemDefault())
                    )

                    return OrderFull(
                        id = order.id,
                        startAddress = "${order.startAddress.address}, ${order.startAddress.city}",
                        endAddress = "${order.endAddress.address}, ${order.endAddress.city}",
                        orderDate = date,
                        orderTime = time,
                        price = "${order.price.amount / 100f} ${order.price.currency}",
                        driverName = order.vehicle.driverName,
                        carNumber = order.vehicle.regNumber.uppercase().take(6),
                        carRegion = order.vehicle.regNumber.drop(6),
                        carModel = order.vehicle.modelName,
                        carPhoto = order.vehicle.photo
                    )
                }
            }
        } catch (e: Exception) {
            return null
        }
    }
}