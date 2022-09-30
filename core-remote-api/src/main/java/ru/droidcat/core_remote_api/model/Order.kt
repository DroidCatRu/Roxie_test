package ru.droidcat.core_remote_api.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Long,
    val startAddress: Address,
    val endAddress: Address,
    val price: Price,
    val orderTime: String,
    val vehicle: Vehicle
)