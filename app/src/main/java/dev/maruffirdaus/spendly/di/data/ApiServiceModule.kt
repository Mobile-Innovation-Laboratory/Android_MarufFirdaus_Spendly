package dev.maruffirdaus.spendly.di.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.data.remote.ItadApiConfig
import dev.maruffirdaus.spendly.data.remote.ItadApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideItadApiService(): ItadApiService =
        ItadApiConfig.getApiService()
}