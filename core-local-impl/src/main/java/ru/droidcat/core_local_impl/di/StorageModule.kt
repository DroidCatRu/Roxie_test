package ru.droidcat.core_local_impl.di

import android.graphics.Bitmap
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_local_api.StorageRepository
import ru.droidcat.core_local_impl.StorageRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageModule {

    @Binds
    @Singleton
    fun provideStorageRepository(impl: StorageRepositoryImpl): StorageRepository
}