package ru.droidcat.core_remote_api.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val city: String,
    val address: String
)
