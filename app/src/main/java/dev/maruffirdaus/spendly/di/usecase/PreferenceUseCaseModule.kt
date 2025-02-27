package dev.maruffirdaus.spendly.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.domain.repository.PreferenceRepository
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetSelectedWalletIdUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveSelectedWalletIdUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceUseCaseModule {
    @Provides
    @Singleton
    fun provideSaveSelectedWalletIdUseCase(repository: PreferenceRepository): SaveSelectedWalletIdUseCase =
        SaveSelectedWalletIdUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSelectedWalletIdUseCase(repository: PreferenceRepository): GetSelectedWalletIdUseCase =
        GetSelectedWalletIdUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveDataSyncEnabledUseCase(repository: PreferenceRepository): SaveDataSyncEnabledUseCase =
        SaveDataSyncEnabledUseCase(repository)

    @Provides
    @Singleton
    fun provideGetDataSyncEnabledUseCase(repository: PreferenceRepository): GetDataSyncEnabledUseCase =
        GetDataSyncEnabledUseCase(repository)
}