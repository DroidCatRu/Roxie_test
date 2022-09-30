package ru.droidcat.core_remote_api.model

import kotlinx.serialization.Serializable

@Serializable
data class Price(
    val amount: Long,
    val currency: String
)
