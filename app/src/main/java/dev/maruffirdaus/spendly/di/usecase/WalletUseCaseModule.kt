package dev.maruffirdaus.spendly.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.domain.repository.FirebaseWalletRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import dev.maruffirdaus.spendly.domain.usecase.wallet.AddEditWalletUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.DeleteWalletUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.GetWalletUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.GetWalletsUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.SyncWalletsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalletUseCaseModule {
    @Provides
    @Singleton
    fun provideAddEditWalletUseCase(repository: WalletRepository): AddEditWalletUseCase =
        AddEditWalletUseCase(repository)

    @Provides
    @Singleton
    fun provideGetWalletsUseCase(repository: WalletRepository): GetWalletsUseCase =
        GetWalletsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetWalletUseCase(repository: WalletRepository): GetWalletUseCase =
        GetWalletUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteWalletUseCase(repository: WalletRepository): DeleteWalletUseCase =
        DeleteWalletUseCase(repository)

    @Provides
    @Singleton
    fun provideSyncWalletsUseCase(repository: FirebaseWalletRepository) =
        SyncWalletsUseCase(repository)
}