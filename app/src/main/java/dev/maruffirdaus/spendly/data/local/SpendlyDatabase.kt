package dev.maruffirdaus.spendly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.maruffirdaus.spendly.data.local.dao.ActivityDao
import dev.maruffirdaus.spendly.data.local.dao.CategoryDao
import dev.maruffirdaus.spendly.data.local.dao.WalletDao
import dev.maruffirdaus.spendly.data.local.model.ActivityEntity
import dev.maruffirdaus.spendly.data.local.model.CategoryEntity
import dev.maruffirdaus.spendly.data.local.model.WalletEntity

@Database(
    entities = [WalletEntity::class, CategoryEntity::class, ActivityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SpendlyDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun categoryDao(): CategoryDao
    abstract fun activityDao(): ActivityDao
}