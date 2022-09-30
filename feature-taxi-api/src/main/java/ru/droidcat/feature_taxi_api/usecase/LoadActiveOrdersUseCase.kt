package ru.droidcat.feature_taxi_api.usecase

import ru.droidcat.core_remote_api.LoadResult
import ru.droidcat.core_remote_api.RemoteRepository
import ru.droidcat.feature_taxi_api.model.OrderBasic
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LoadActiveOrdersUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend operator fun invoke(): List<OrderBasic>? {
        when (val loadResult = remoteRepository.loadActiveOrders()) {
            is LoadResult.ERROR -> {
                return null
            }
            is LoadResult.SUCCESS -> {
                return loadResult.data
                    .sortedByDescending {
                        it.orderTime
                    }
                    .map {

                        val dateTime =
                            ZonedDateTime.parse(it.orderTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

                        val date = dateTime.format(
                            DateTimeFormatter
                                .ofPattern("dd.MM.yyyy")
                                .withZone(ZoneId.systemDefault())
                        )

                        OrderBasic(
                            id = it.id,
                            startAddress = "${it.startAddress.address}, ${it.startAddress.city}",
                            endAddress = "${it.endAddress.address}, ${it.endAddress.city}",
                            orderDate = date,
                            price = "${it.price.amount / 100f} ${it.price.currency}"
                        )
                    }
            }
        }
    }
}