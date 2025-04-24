package io.github.saidooubella.display.ninepad

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.Channel

interface Pipe<out T> : AutoCloseable {
   suspend fun receive(block: (T) -> Unit)
}

interface MutablePipe<T> : Pipe<T> {
   fun send(value: T)
}

private class ReadOnlyPipe<T>(val pipeline: Pipe<T>) : Pipe<T> {
   override suspend fun receive(block: (T) -> Unit) = pipeline.receive(block)
   override fun close() = pipeline.close()
}

fun <T> MutablePipe<T>.asPipe(): Pipe<T> = ReadOnlyPipe(this)

private class PipeImpl<T> : MutablePipe<T> {

   private val channel = Channel<T>()

   override fun send(value: T) {
      channel.trySend(value)
   }

   override suspend fun receive(block: (T) -> Unit) {
      for (value in channel) block(value)
   }

   override fun close() {
      channel.close()
   }
}

@Composable
fun <T> rememberMutablePipeOf(): MutablePipe<T> {
   val pipe = remember { PipeImpl<T>() }
   DisposableEffect(Unit) { onDispose { pipe.close() } }
   return pipe
}
