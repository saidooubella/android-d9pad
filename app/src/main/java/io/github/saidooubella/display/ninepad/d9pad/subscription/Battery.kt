package io.github.saidooubella.display.ninepad.d9pad.subscription

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import kotlinx.coroutines.flow.Flow

fun battery(context: Context): Flow<Int> {
   return receiver(context, IntentFilter(Intent.ACTION_BATTERY_CHANGED)) { intent, emit ->
      val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
      val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
      if (level >= 0 && scale > 0) emit((level * 100) / scale)
   }
}
