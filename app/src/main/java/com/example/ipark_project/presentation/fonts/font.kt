package com.example.ipark_project.presentation.fonts// ...
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import com.example.ipark_project.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val fontName = GoogleFont("Poppins")

val LandingText = FontFamily(
    Font(googleFont = fontName, fontProvider = provider, weight = FontWeight.Bold)
)
fun landingText(): FontFamily {
    return LandingText
}