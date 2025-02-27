package dev.maruffirdaus.spendly.di.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.maruffirdaus.spendly.data.local.SpendlyDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SpendlyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SpendlyDatabase::class.java,
            "spendly_database.db"
        ).build()
    }

    @Provides
    fun provideWalletDao(database: SpendlyDatabase) = database.walletDao()

    @Provides
    fun provideCategoryDao(database: SpendlyDatabase) = database.categoryDao()

    @Provides
    fun provideActivityDao(database: SpendlyDatabase) = database.activityDao()
}