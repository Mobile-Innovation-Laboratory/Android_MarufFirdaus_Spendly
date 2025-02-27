package dev.maruffirdaus.spendly

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import dagger.hilt.android.HiltAndroidApp

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")

@HiltAndroidApp
class SpendlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        EmojiCompat.init(BundledEmojiCompatConfig(this, mainExecutor).setReplaceAll(true))
    }
}