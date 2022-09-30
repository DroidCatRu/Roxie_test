package ru.droidcat.feature_taxi_api.intent

import ru.droidcat.core_navigation.FeatureIntent

data class TaxiOrderChosen(val order_id: Long) : FeatureIntent()
