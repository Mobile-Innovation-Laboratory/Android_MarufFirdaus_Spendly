package dev.maruffirdaus.spendly.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.domain.repository.CategoryRepository
import dev.maruffirdaus.spendly.domain.repository.FirebaseCategoryRepository
import dev.maruffirdaus.spendly.domain.usecase.category.AddEditCategoryUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.DeleteCategoryUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.GetCategoriesUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.SyncCategoriesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryUseCaseModule {
    @Provides
    @Singleton
    fun provideAddEditCategoryUseCase(repository: CategoryRepository): AddEditCategoryUseCase =
        AddEditCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: CategoryRepository): GetCategoriesUseCase =
        GetCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCategoryUseCase(repository: CategoryRepository): DeleteCategoryUseCase =
        DeleteCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideSyncCategoriesUseCase(repository: FirebaseCategoryRepository) =
        SyncCategoriesUseCase(repository)
}