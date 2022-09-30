package ru.droidcat.core_navigation

import kotlinx.coroutines.flow.Flow

interface FeatureIntentManager {
    val featureIntent: Flow<FeatureIntent>
    fun sendIntent(intent: FeatureIntent)
}