package ru.droidcat.feature_taxi_api.model

data class OrderBasic(
    val id: Long,
    val startAddress: String,
    val endAddress: String,
    val orderDate: String,
    val price: String
)
