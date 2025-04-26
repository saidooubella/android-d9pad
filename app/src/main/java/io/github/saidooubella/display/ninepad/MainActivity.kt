package io.github.saidooubella.display.ninepad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.saidooubella.display.ninepad.d9pad.renderer.StaticRender
import io.github.saidooubella.display.ninepad.ui.theme.D9PadTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         D9PadTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
               Column(Modifier.padding(innerPadding)) {
                  val keystrokes = rememberMutablePipeOf<KeypadCode>()
//                  DialerSample(keystrokes, Modifier.weight(1F))
                  Box(Modifier.weight(1f)) {
                     val context = LocalContext.current
                     val uiHost = remember(context) { SysUiHost(context) }
                     StaticRender(uiHost)
                  }
                  Keypad(Modifier.align(Alignment.CenterHorizontally), keystrokes::send)
               }
            }
         }
      }
   }
}

private val keycodeToChar = buildMap {
   put(KeypadCode.Key0, '0')
   put(KeypadCode.Key1, '1')
   put(KeypadCode.Key2, '2')
   put(KeypadCode.Key3, '3')
   put(KeypadCode.Key4, '4')
   put(KeypadCode.Key5, '5')
   put(KeypadCode.Key6, '6')
   put(KeypadCode.Key7, '7')
   put(KeypadCode.Key8, '8')
   put(KeypadCode.Key9, '9')
   put(KeypadCode.Star, '*')
   put(KeypadCode.Pound, '#')
}

@Composable
private fun DialerSample(
   keystrokes: Pipe<KeypadCode>,
   modifier: Modifier = Modifier,
) {
   Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
   ) {

      var number by rememberSaveable { mutableStateOf("") }
      var cursor by rememberSaveable { mutableIntStateOf(0) }

      LaunchedEffect(keystrokes) {
         keystrokes.receive { keycode ->
            when (keycode) {
               in keycodeToChar -> {
                  val char = keycodeToChar.getValue(keycode)
                  number = number.insert(char, cursor)
                  cursor += 1
               }

               KeypadCode.SoftRight -> {
                  if (number.isNotEmpty() && cursor > 0) {
                     number = number.remove(cursor)
                     cursor -= 1
                  }
               }

               KeypadCode.Right -> {
                  if (cursor < number.length) cursor += 1
               }

               KeypadCode.Left -> {
                  if (cursor > 0) cursor -= 1
               }

               else -> {}
            }
         }
      }

      val blink by produceState(true) {
         while (isActive) {
            delay(500)
            value = !value
         }
      }

      val textMeasurer = rememberTextMeasurer()
      val colors = MaterialTheme.colorScheme

      CanvasWithCache(
         Modifier
            .padding(16.dp)
            .fillMaxSize()
      ) {

         val layoutResult = textMeasurer.measure(
            text = number,
            style = TextStyle(fontSize = 64.sp, color = colors.onBackground),
            constraints = Constraints(maxWidth = size.width.toInt())
         )

         var cursorRect = if (number.isEmpty()) {
            Rect(0f, 0f, 0f, layoutResult.size.height.toFloat())
         } else if (cursor == number.length) {
            val rect = layoutResult.getBoundingBox(number.lastIndex)
            Rect(rect.right, rect.top, rect.right, rect.bottom)
         } else {
            val rect = layoutResult.getBoundingBox(cursor)
            Rect(rect.left, rect.top, rect.left, rect.bottom)
         }

         val cursorCenterY = (cursorRect.top + cursorRect.bottom) / 2
         val textHeight = layoutResult.size.height.toFloat()
         val canvasHeight = size.height

         val scrollOffsetY = when {
            textHeight <= canvasHeight || cursorCenterY < canvasHeight / 2f -> 0f
            cursorCenterY > textHeight - canvasHeight / 2f -> textHeight - canvasHeight
            else -> cursorCenterY - canvasHeight / 2f
         }

         val topLeft = Offset(0f, -scrollOffsetY)

         cursorRect = cursorRect.translate(Offset(0f, -scrollOffsetY))

         onDrawBehind {

            clipRect { drawText(layoutResult, topLeft = topLeft) }

            if (!blink) return@onDrawBehind

            drawLine(
               color = colors.onBackground,
               start = cursorRect.topLeft,
               end = cursorRect.bottomRight,
               strokeWidth = 2f,
            )
         }
      }
   }
}

@Composable
private fun CanvasWithCache(modifier: Modifier, onBuildDrawCache: CacheDrawScope.() -> DrawResult) {
   Spacer(modifier.drawWithCache(onBuildDrawCache))
}

@Preview
@Composable
private fun Text() {
   StaticRender(SysUiHost(LocalContext.current))
}