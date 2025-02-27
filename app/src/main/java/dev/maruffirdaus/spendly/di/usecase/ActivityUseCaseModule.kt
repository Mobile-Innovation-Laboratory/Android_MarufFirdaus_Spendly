package dev.maruffirdaus.spendly.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import dev.maruffirdaus.spendly.domain.repository.FirebaseActivityRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import dev.maruffirdaus.spendly.domain.usecase.activity.AddEditActivityUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.DeleteActivityUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.GetActivitiesUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.GetActivityUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.SyncActivitiesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ActivityUseCaseModule {
    @Provides
    @Singleton
    fun provideAddEditActivityUseCase(
        walletRepository: WalletRepository,
        activityRepository: ActivityRepository
    ): AddEditActivityUseCase = AddEditActivityUseCase(walletRepository, activityRepository)

    @Provides
    @Singleton
    fun provideGetActivitiesUseCase(repository: ActivityRepository): GetActivitiesUseCase =
        GetActivitiesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetActivityUseCase(repository: ActivityRepository): GetActivityUseCase =
        GetActivityUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteActivityUseCase(
        walletRepository: WalletRepository,
        activityRepository: ActivityRepository
    ): DeleteActivityUseCase = DeleteActivityUseCase(walletRepository, activityRepository)

    @Provides
    @Singleton
    fun provideSyncActivitiesUseCase(repository: FirebaseActivityRepository) =
        SyncActivitiesUseCase(repository)
}