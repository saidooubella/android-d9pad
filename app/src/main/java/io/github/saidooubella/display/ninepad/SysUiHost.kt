package io.github.saidooubella.display.ninepad

import android.content.Context
import io.github.saidooubella.display.ninepad.d9pad.subscription.battery
import io.github.saidooubella.display.ninepad.d9pad.subscription.time
import io.github.saidooubella.display.ninepad.d9pad.view.Column
import io.github.saidooubella.display.ninepad.d9pad.view.Host
import io.github.saidooubella.display.ninepad.d9pad.view.Row
import io.github.saidooubella.display.ninepad.d9pad.view.Text
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Padding
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Size
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.SizePolicy.Exact
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.SizePolicy.MatchParent
import kotlinx.coroutines.flow.map

data class SysUiState(
   val time: String = "",
   val battery: Int = 0,
)

sealed interface SysUiEvent {
   data class Time(val time: String) : SysUiEvent
   data class Battery(val level: Int) : SysUiEvent
}

class SysUiHost(context: Context) : Host<SysUiState, SysUiEvent>() {

   override val initialState = SysUiState()

   override val subscriptions = listOf(
      battery(context).map(SysUiEvent::Battery),
      time().map(SysUiEvent::Time),
   )

   override fun view(state: SysUiState) = Column(
      modifier = Padding(horizontal = 32)
   ) {
      +Row(
         horizontalArrangement = Row.Arrangement.SpaceBetween,
         modifier = Size(width = MatchParent),
      ) {
         +Text(state.time)
         +Text("${state.battery}%")
      }
      +Column(
         verticalArrangement = Column.Arrangement.Center,
         horizontalAlignment = Column.Alignment.CenterHorizontally,
         modifier = Size(height = Exact(400), width = MatchParent)
      ) {
         +Text("Hello, world!")
      }
      +Row(
         horizontalArrangement = Row.Arrangement.SpaceBetween,
         modifier = Size(width = MatchParent),
      ) {
         +Text("Menu")
         +Text("Select")
         +Text("Back")
      }
   }

   override fun update(event: SysUiEvent, state: SysUiState) = when (event) {
      is SysUiEvent.Battery -> state.copy(battery = event.level)
      is SysUiEvent.Time -> state.copy(time = event.time)
   }
}
