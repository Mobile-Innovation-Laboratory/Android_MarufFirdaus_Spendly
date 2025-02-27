package dev.maruffirdaus.spendly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.maruffirdaus.spendly.R

val nunitoFontFamily = FontFamily(
    Font(R.font.nunito_extra_light, FontWeight.ExtraLight),
    Font(R.font.nunito_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
    Font(R.font.nunito_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_bold_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.nunito_extra_bold, FontWeight.ExtraBold),
    Font(R.font.nunito_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.nunito_black, FontWeight.Black),
    Font(R.font.nunito_black_italic, FontWeight.Black, FontStyle.Italic)
)

val baseline = Typography()

val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = nunitoFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = nunitoFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = nunitoFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = nunitoFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = nunitoFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = nunitoFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = nunitoFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = nunitoFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = nunitoFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = nunitoFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = nunitoFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = nunitoFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = nunitoFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = nunitoFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = nunitoFontFamily)
)