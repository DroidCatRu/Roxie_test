package ru.droidcat.core_remote_impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import ru.droidcat.core_remote_api.RemoteRepository
import ru.droidcat.core_remote_impl.RemoteRepositoryImpl
import javax.inject.Singleton

@Module(includes = [RemoteModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    internal fun provideKtorClient(): HttpClient {
        return HttpClient() {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun provideRemoteRepository(impl: RemoteRepositoryImpl): RemoteRepository
    }
}