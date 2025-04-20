package io.github.saidooubella.display.ninepad

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.Channel

interface Pipeline<out T> : AutoCloseable {
   suspend fun receive(block: (T) -> Unit)
}

interface MutablePipeline<T> : Pipeline<T> {
   fun send(value: T)
}

private class ReadOnlyPipeline<T>(val pipeline: Pipeline<T>) : Pipeline<T> {
   override suspend fun receive(block: (T) -> Unit) = pipeline.receive(block)
   override fun close() = pipeline.close()
}

fun <T> MutablePipeline<T>.asPipeline(): Pipeline<T> = ReadOnlyPipeline(this)

private class PipelineImpl<T> : MutablePipeline<T> {

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
fun <T> rememberMutablePipelineOf(): MutablePipeline<T> {
   val pipeline = remember { PipelineImpl<T>() }

   DisposableEffect(Unit) {
      onDispose { pipeline.close() }
   }

   return pipeline
}
