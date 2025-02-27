package dev.maruffirdaus.spendly.di.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.data.local.dao.ActivityDao
import dev.maruffirdaus.spendly.data.local.dao.CategoryDao
import dev.maruffirdaus.spendly.data.local.dao.WalletDao
import dev.maruffirdaus.spendly.data.remote.ItadApiService
import dev.maruffirdaus.spendly.data.repository.ActivityRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.CategoryRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.DealRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.AuthRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.FirebaseActivityRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.FirebaseCategoryRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.FirebaseWalletRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.PreferenceRepositoryImpl
import dev.maruffirdaus.spendly.data.repository.WalletRepositoryImpl
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import dev.maruffirdaus.spendly.domain.repository.CategoryRepository
import dev.maruffirdaus.spendly.domain.repository.DealRepository
import dev.maruffirdaus.spendly.domain.repository.AuthRepository
import dev.maruffirdaus.spendly.domain.repository.FirebaseActivityRepository
import dev.maruffirdaus.spendly.domain.repository.FirebaseCategoryRepository
import dev.maruffirdaus.spendly.domain.repository.FirebaseWalletRepository
import dev.maruffirdaus.spendly.domain.repository.PreferenceRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun providePreferenceRepository(dataStore: DataStore<Preferences>): PreferenceRepository =
        PreferenceRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideWalletRepository(dao: WalletDao): WalletRepository =
        WalletRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideFirebaseWalletRepository(
        firestore: FirebaseFirestore,
        dao: WalletDao
    ): FirebaseWalletRepository = FirebaseWalletRepositoryImpl(firestore, dao)

    @Provides
    @Singleton
    fun provideCategoryRepository(dao: CategoryDao): CategoryRepository =
        CategoryRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideFirebaseCategoryRepository(
        firestore: FirebaseFirestore,
        dao: CategoryDao
    ): FirebaseCategoryRepository = FirebaseCategoryRepositoryImpl(firestore, dao)

    @Provides
    @Singleton
    fun provideActivityRepository(dao: ActivityDao): ActivityRepository =
        ActivityRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideFirebaseActivityRepository(
        firestore: FirebaseFirestore,
        dao: ActivityDao
    ): FirebaseActivityRepository = FirebaseActivityRepositoryImpl(firestore, dao)

    @Provides
    @Singleton
    fun provideDealRepository(itadApiService: ItadApiService): DealRepository =
        DealRepositoryImpl(itadApiService)
}