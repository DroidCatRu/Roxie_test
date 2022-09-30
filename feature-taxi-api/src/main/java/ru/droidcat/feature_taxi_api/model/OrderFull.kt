package ru.droidcat.feature_taxi_api.model

data class OrderFull(
    val id: Long,
    val startAddress: String,
    val endAddress: String,
    val orderDate: String,
    val orderTime: String,
    val price: String,
    val driverName: String,
    val carNumber: String,
    val carRegion: String,
    val carModel: String,
    val carPhoto: String
)
