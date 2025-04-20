package io.github.saidooubella.display.ninepad

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HorizontalRule
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PhoneEnabled
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Gray = Color(0xFF757575)
private val Green = Color(0xFF4CAF50)
private val Red = Color(0xFFF44336)

@Composable
fun Keypad(
   modifier: Modifier = Modifier,
   onKeyPress: (KeypadCode) -> Unit,
) {
   val hapticFeedback = LocalHapticFeedback.current
   val updatedOnKeyPress by rememberUpdatedState(onKeyPress)

   val scope = rememberCoroutineScope()

   val pressWithHaptics = remember(hapticFeedback) {
      { keycode: KeypadCode ->
         hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
         updatedOnKeyPress(keycode)
      }
   }

   val focusRequester = remember { FocusRequester() }
   LaunchedEffect(Unit) { focusRequester.requestFocus() }

   Column(
      modifier
         .sizeIn(maxWidth = 480.dp)
         .focusRequester(focusRequester)
         .focusable()
         .onKeyEvent { keyEvent ->
            when (val keycode = keyEvent.key.toKeypadCode()) {
               null -> false
               else -> {
                  pressWithHaptics(keycode)
                  true
               }
            }
         },
   ) {
      ControlButtons(scope = scope, onKeyPress = pressWithHaptics)
      NumPad(scope = scope, onKeyPress = pressWithHaptics)
   }
}

@Composable
private fun ControlButtons(
   scope: CoroutineScope,
   modifier: Modifier = Modifier,
   onKeyPress: (KeypadCode) -> Unit,
) {
   Row(modifier.height(IntrinsicSize.Max)) {

      val controlButtonStyle = Modifier.fillMaxWidth()
      val containerStyle = Modifier.weight(1F)

      Column(
         modifier = containerStyle,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         ControlButton(
            scope = scope,
            icon = Icons.Outlined.HorizontalRule,
            modifier = controlButtonStyle,
            color = Gray,
         ) {
            onKeyPress(KeypadCode.SoftLeft)
         }
         ControlButton(
            scope = scope,
            icon = Icons.Outlined.Phone,
            modifier = controlButtonStyle,
            color = Green,
         ) {
            onKeyPress(KeypadCode.Call)
         }
      }
      Column(
         modifier = containerStyle,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         DPad(
            scope = scope,
            modifier = Modifier.fillMaxHeight(),
            onKeyPress = onKeyPress,
         )
      }
      Column(
         modifier = containerStyle,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         ControlButton(
            scope = scope,
            icon = Icons.Outlined.HorizontalRule,
            modifier = controlButtonStyle,
            color = Gray,
         ) {
            onKeyPress(KeypadCode.SoftRight)
         }
         ControlButton(
            scope = scope,
            icon = Icons.Outlined.PhoneEnabled,
            modifier = controlButtonStyle,
            color = Red,
         ) {
            onKeyPress(KeypadCode.End)
         }
      }
   }
}

@Composable
private fun ControlButton(
   scope: CoroutineScope,
   icon: ImageVector,
   color: Color,
   modifier: Modifier = Modifier,
   onPress: () -> Unit,
) {
   Box(
      modifier = modifier
         .height(64.dp)
         .continuousClick(scope, onClick = onPress),
      contentAlignment = Alignment.Center,
   ) {
      Icon(icon, contentDescription = null, tint = color)
   }
}

@Composable
private fun DPad(
   scope: CoroutineScope,
   modifier: Modifier = Modifier,
   onKeyPress: (KeypadCode) -> Unit,
) {
   val size = 90.dp

   Box(
      modifier = modifier
         .size(size)
         .drawBehind {
            drawCircle(Gray, style = Stroke(width = 2.dp.toPx()))
         },
      contentAlignment = Alignment.Center
   ) {
      DPadButton(
         scope = scope,
         modifier = Modifier.offset(y = -(size / 2)),
         onPress = { onKeyPress(KeypadCode.Up) },
      )
      DPadButton(
         scope = scope,
         modifier = Modifier.offset(y = size / 2),
         onPress = { onKeyPress(KeypadCode.Down) },
      )
      DPadButton(
         scope = scope,
         modifier = Modifier.offset(x = -(size / 2)),
         onPress = { onKeyPress(KeypadCode.Left) },
      )
      DPadButton(
         scope = scope,
         modifier = Modifier.offset(x = size / 2),
         onPress = { onKeyPress(KeypadCode.Right) },
      )
      DPadButton(
         scope = scope,
         modifier = Modifier.size(48.dp),
         onPress = { onKeyPress(KeypadCode.Center) },
      ) {
         Text("OK", fontWeight = FontWeight.Bold)
      }
   }
}

@Composable
private fun DPadButton(
   scope: CoroutineScope,
   modifier: Modifier = Modifier,
   onPress: () -> Unit,
   content: (@Composable () -> Unit)? = null,
) {
   Box(
      modifier = modifier
         .size(48.dp)
         .clip(CircleShape)
         .continuousClick(scope, onClick = onPress),
      contentAlignment = Alignment.Center,
   ) {
      content?.invoke()
   }
}

@Composable
private fun NumPad(
   scope: CoroutineScope,
   modifier: Modifier = Modifier,
   onKeyPress: (KeypadCode) -> Unit,
) {
   Row(modifier.height(IntrinsicSize.Max)) {

      val keypadColumnStyle = Modifier
         .weight(1F)
         .width(IntrinsicSize.Max)
         .fillMaxHeight()

      val keypadButtonStyle = Modifier
         .weight(1F)
         .fillMaxWidth()

      Column(
         modifier = keypadColumnStyle,
         horizontalAlignment = Alignment.CenterHorizontally,
      ) {
         NumPadButton(text = "1", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key1)
         }
         NumPadButton(text = "4", subText = "GHI", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key4)
         }
         NumPadButton(text = "7", subText = "PQRS", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key7)
         }
         NumPadButton(text = "＊", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Star)
         }
      }
      Column(
         modifier = keypadColumnStyle,
         horizontalAlignment = Alignment.CenterHorizontally,
      ) {
         NumPadButton(text = "2", subText = "ABC", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key2)
         }
         NumPadButton(text = "5", subText = "JKL", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key5)
         }
         NumPadButton(text = "8", subText = "TUV", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key8)
         }
         NumPadButton(text = "0", subText = "␣", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key0)
         }
      }
      Column(
         modifier = keypadColumnStyle,
         horizontalAlignment = Alignment.CenterHorizontally,
      ) {
         NumPadButton(text = "3", subText = "DEF", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key3)
         }
         NumPadButton(text = "6", subText = "MNO", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key6)
         }
         NumPadButton(text = "9", subText = "WAXY", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Key9)
         }
         NumPadButton(text = "#", scope = scope, modifier = keypadButtonStyle) {
            onKeyPress(KeypadCode.Pound)
         }
      }
   }
}

@Composable
private fun NumPadButton(
   text: String,
   scope: CoroutineScope,
   modifier: Modifier = Modifier,
   subText: String? = null,
   onPress: () -> Unit,
) {
   Column(
      modifier = modifier
         .height(70.dp)
         .fillMaxWidth()
         .continuousClick(scope, onClick = onPress),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
   ) {
      val textStyle = LocalTextStyle.current
      Text(text, style = textStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.Black))
      Spacer(Modifier.width(6.dp))
      if (subText != null) {
         Text(subText, style = textStyle.copy(fontSize = 12.sp))
      }
   }
}

private fun Modifier.continuousClick(
   scope: CoroutineScope,
   initialDelay: Long = 300L,
   repeatInterval: Long = 60L,
   onClick: () -> Unit,
): Modifier = composed {

   val interactionSource = remember { MutableInteractionSource() }
   var job by remember { MutableRef<Job?>(null) }

   this
      .indication(interactionSource, LocalIndication.current)
      .pointerInput(Unit) {
         detectTapGestures(
            onPress = {
               val press = PressInteraction.Press(it)
               interactionSource.emit(press)

               onClick()
               job = scope.launch {
                  delay(initialDelay)
                  while (true) {
                     onClick()
                     delay(repeatInterval)
                  }
               }

               try {
                  awaitRelease()
               } finally {
                  job?.cancel()
                  interactionSource.emit(PressInteraction.Release(press))
               }
            }
         )
      }
}
