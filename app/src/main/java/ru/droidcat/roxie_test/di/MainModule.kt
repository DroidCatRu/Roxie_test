package ru.droidcat.roxie_test.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_navigation.FeatureIntentManager
import ru.droidcat.roxie_test.FeatureIntentManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MainModule {

    @Binds
    @Singleton
    fun bindFeatureIntentManager(impl: FeatureIntentManagerImpl): FeatureIntentManager
}