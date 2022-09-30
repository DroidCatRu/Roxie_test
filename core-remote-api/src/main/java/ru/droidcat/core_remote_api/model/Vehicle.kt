package ru.droidcat.core_remote_api.model

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val regNumber: String,
    val modelName: String,
    val photo: String,
    val driverName: String
)
