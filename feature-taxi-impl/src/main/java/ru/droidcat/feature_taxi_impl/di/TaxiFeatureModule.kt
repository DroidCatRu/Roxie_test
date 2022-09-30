package ru.droidcat.feature_taxi_impl.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.droidcat.core_navigation.NavigationFactory
import ru.droidcat.feature_taxi_impl.TaxiNavigationFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TaxiFeatureModule {

    @Binds
    @Singleton
    @IntoSet
    fun bindTaxiNavigationFactory(factory: TaxiNavigationFactory): NavigationFactory
}