package dev.maruffirdaus.spendly.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import dev.maruffirdaus.spendly.domain.repository.AuthRepository
import dev.maruffirdaus.spendly.domain.repository.CategoryRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.SignInUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.SignOutUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.SignUpUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {
    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase =
        SignInUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase =
        SignUpUseCase(repository)

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authRepository: AuthRepository,
        walletRepository: WalletRepository,
        categoryRepository: CategoryRepository,
        activityRepository: ActivityRepository
    ): SignOutUseCase =
        SignOutUseCase(authRepository, walletRepository, categoryRepository, activityRepository)

    @Provides
    @Singleton
    fun provideGetUserUseCase(repository: AuthRepository): GetUserUseCase =
        GetUserUseCase(repository)
}