package dev.maruffirdaus.spendly.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.domain.repository.DealRepository
import dev.maruffirdaus.spendly.domain.usecase.deal.GetDealsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DealUseCaseModule {
    @Provides
    @Singleton
    fun provideGetDealsUseCase(repository: DealRepository): GetDealsUseCase =
        GetDealsUseCase(repository)
}