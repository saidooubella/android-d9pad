package io.github.saidooubella.display.ninepad.ui.theme

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
   primary = Purple80,
   secondary = PurpleGrey80,
   tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
   primary = Purple40,
   secondary = PurpleGrey40,
   tertiary = Pink40,
)

@Composable
fun D9PadTheme(
   darkTheme: Boolean = isSystemInDarkTheme(),
   content: @Composable () -> Unit,
) {
   val colorScheme = when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> LocalContext.current.let { context ->
         if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      else -> if (darkTheme) DarkColorScheme else LightColorScheme
   }

   MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}