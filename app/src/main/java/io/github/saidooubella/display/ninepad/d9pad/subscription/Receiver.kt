package io.github.saidooubella.display.ninepad.d9pad.subscription

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

typealias ReceiverBlock<Event> = (intent: Intent, emit: (Event) -> Unit) -> Unit

inline fun <Event> receiver(
   context: Context,
   filter: IntentFilter,
   crossinline block: ReceiverBlock<Event>,
): Flow<Event> {
   return callbackFlow {
      val receiver = object : BroadcastReceiver() {
         override fun onReceive(ctx: Context, intent: Intent) = block(intent, ::trySend)
      }
      ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
      awaitClose { context.unregisterReceiver(receiver) }
   }
}
