package io.github.saidooubella.display.ninepad.d9pad.subscription

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun timer(interval: Long) = flow {
   emit(Unit)
   while (true) {
      delay(interval)
      emit(Unit)
   }
}
