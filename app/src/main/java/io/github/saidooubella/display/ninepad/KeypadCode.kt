package io.github.saidooubella.display.ninepad

import androidx.compose.ui.input.key.Key

sealed interface KeypadCode {

   data object Key0 : KeypadCode
   data object Key1 : KeypadCode
   data object Key2 : KeypadCode
   data object Key3 : KeypadCode
   data object Key4 : KeypadCode
   data object Key5 : KeypadCode
   data object Key6 : KeypadCode
   data object Key7 : KeypadCode
   data object Key8 : KeypadCode
   data object Key9 : KeypadCode

   data object Star : KeypadCode
   data object Pound : KeypadCode

   data object Up : KeypadCode
   data object Down : KeypadCode
   data object Left : KeypadCode
   data object Right : KeypadCode
   data object Center : KeypadCode

   data object SoftLeft : KeypadCode
   data object SoftRight : KeypadCode

   data object VolumeUp : KeypadCode
   data object VolumeDown : KeypadCode

   data object Call : KeypadCode
   data object End : KeypadCode
}

fun Key.toKeypadCode(): KeypadCode? {
   return when (this) {
      Key.VolumeUp -> KeypadCode.VolumeUp
      Key.VolumeDown -> KeypadCode.VolumeDown
      Key.DirectionUp -> KeypadCode.Up
      Key.DirectionDown -> KeypadCode.Down
      Key.DirectionLeft -> KeypadCode.Left
      Key.DirectionRight -> KeypadCode.Right
      else -> null
   }
}
