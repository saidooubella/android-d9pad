package io.github.saidooubella.display.ninepad.d9pad.view

import kotlinx.coroutines.flow.Flow

abstract class Host<State, Event> : View<Nothing> {
   abstract val initialState: State
   open val subscriptions: List<Flow<Event>> get() = emptyList()
   abstract fun view(state: State): View<Event>
   abstract fun update(event: Event, state: State): State
}
