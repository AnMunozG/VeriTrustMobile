package com.example.veritrustmobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = LightBlueBackground,
    background = DarkBlue,
    surface = DarkBlue,
    error = ErrorRedDark,
    onPrimary = PureWhite,
    onSecondary = TextGrey,
    onBackground = PureWhite,
    onSurface = PureWhite,
    onError = PureWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = DarkBlue,
    background = OffWhiteBackground,
    surface = OffWhiteBackground,
    error = ErrorRed,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onBackground = TextGrey,
    onSurface = TextGrey,
    onError = PureWhite

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun VeriTrustMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
