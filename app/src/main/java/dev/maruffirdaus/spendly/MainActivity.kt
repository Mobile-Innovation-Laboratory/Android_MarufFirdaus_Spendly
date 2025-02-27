package dev.maruffirdaus.spendly

import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.maruffirdaus.spendly.ui.SpendlyNavHost
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.util.WindowInfo
import dev.maruffirdaus.spendly.ui.util.rememberWindowInfo

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        enableEdgeToEdge()
        setContent {
            SpendlyTheme {
                val windowInfo = rememberWindowInfo()
                val screenWidthInfo = windowInfo.screenWidthInfo
                val screenHeightInfo = windowInfo.screenHeightInfo
                val isCompactScreen =
                    screenWidthInfo is WindowInfo.WindowType.Compact || screenHeightInfo is WindowInfo.WindowType.Compact

                SpendlyNavHost(
                    country = telephonyManager.networkCountryIso.uppercase(),
                    compactScreen = isCompactScreen
                )
            }
        }
    }
}