package io.github.saidooubella.display.ninepad.d9pad.renderer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.saidooubella.display.ninepad.d9pad.view.Box
import io.github.saidooubella.display.ninepad.d9pad.view.Button
import io.github.saidooubella.display.ninepad.d9pad.view.Column
import io.github.saidooubella.display.ninepad.d9pad.view.Field
import io.github.saidooubella.display.ninepad.d9pad.view.Host
import io.github.saidooubella.display.ninepad.d9pad.view.Row
import io.github.saidooubella.display.ninepad.d9pad.view.View
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Background
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.CombinedModifier
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Padding
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Size
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.SizePolicy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import io.github.saidooubella.display.ninepad.d9pad.view.Text as D9PadText
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Modifier as D9PadModifier

@Composable
fun <State, Event> HostRender(host: Host<State, Event>) {

   var state by remember { mutableStateOf(host.initialState) }
   val eventChannel = remember { MutableSharedFlow<Event>() }

   val scope = rememberCoroutineScope()

   LaunchedEffect(host) {
      eventChannel.collect { event -> state = host.update(event, state) }
   }

   LaunchedEffect(host) {
      host.subscriptions.forEach { flow ->
         launch { flow.collect { event -> eventChannel.emit(event) } }
      }
   }

   Render(host.view(state)) { event ->
      scope.launch { eventChannel.emit(event) }
   }
}

@Composable
fun StaticRender(view: View<Nothing>) = Render(view)

/////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun <Event> Render(view: View<Event>, update: ((Event) -> Unit)? = null) {
   when (view) {
      is Column<Event> -> RenderColumn(view, update)
      is Button<Event> -> RenderButton(update, view)
      is Field<Event> -> RenderField(view, update)
      is Box<Event> -> RenderBox(view, update)
      is Row<Event> -> RenderRow(view, update)
      is Host<*, *> -> HostRender(view)
      is D9PadText -> Text(view.value)
   }
}

/////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun <Event> RenderBox(view: Box<Event>, update: ((Event) -> Unit)?) {
   Box(
      contentAlignment = view.contentAlignment.toComposeAlignment(),
      content = { view.children.forEach { Render(it, update) } },
      modifier = view.modifier.toComposeModifier(),
   )
}

private fun Box.Alignment.toComposeAlignment(): Alignment {
   return when (this) {
      Box.Alignment.TopStart -> Alignment.TopStart
      Box.Alignment.TopCenter -> Alignment.TopCenter
      Box.Alignment.TopEnd -> Alignment.TopEnd
      Box.Alignment.CenterStart -> Alignment.CenterStart
      Box.Alignment.Center -> Alignment.Center
      Box.Alignment.CenterEnd -> Alignment.CenterEnd
      Box.Alignment.BottomStart -> Alignment.BottomStart
      Box.Alignment.BottomCenter -> Alignment.BottomCenter
      Box.Alignment.BottomEnd -> Alignment.BottomEnd
      else -> error("Unknown alignment")
   }
}

/////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun <Event> RenderField(view: Field<Event>, update: ((Event) -> Unit)?) {
   TextField(view.value, { update?.invoke(view.onValueChange(it)) })
}

/////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun <Event> RenderButton(update: ((Event) -> Unit)?, view: Button<Event>) {
   Button({ update?.invoke(view.onClick) }) { Text(view.value) }
}

/////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun <Event> RenderRow(view: Row<Event>, update: ((Event) -> Unit)?) {
   Row(
      horizontalArrangement = view.horizontalArrangement.toComposeArrangement(),
      verticalAlignment = view.verticalAlignment.toComposeAlignment(),
      content = { view.children.forEach { Render(it, update) } },
      modifier = view.modifier.toComposeModifier(),
   )
}

private fun Row.Arrangement.toComposeArrangement(): Arrangement.Horizontal {
   return when (this) {
      Row.Arrangement.Start -> Arrangement.Start
      Row.Arrangement.End -> Arrangement.End
      Row.Arrangement.Center -> Arrangement.Center
      Row.Arrangement.SpaceEvenly -> Arrangement.SpaceEvenly
      Row.Arrangement.SpaceBetween -> Arrangement.SpaceBetween
      Row.Arrangement.SpaceAround -> Arrangement.SpaceAround
      else -> error("Unknown arrangement")
   }
}

private fun Row.Alignment.toComposeAlignment(): Alignment.Vertical {
   return when (this) {
      Row.Alignment.CenterVertically -> Alignment.CenterVertically
      Row.Alignment.Bottom -> Alignment.Bottom
      Row.Alignment.Top -> Alignment.Top
      else -> error("Unknown alignment")
   }
}

/////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun <Event> RenderColumn(view: Column<Event>, update: ((Event) -> Unit)?) {
   Column(
      horizontalAlignment = view.horizontalAlignment.toComposeAlignment(),
      verticalArrangement = view.verticalArrangement.toComposeArrangement(),
      content = { view.children.forEach { Render(it, update) } },
      modifier = view.modifier.toComposeModifier(),
   )
}

private fun Column.Arrangement.toComposeArrangement(): Arrangement.Vertical {
   return when (this) {
      Column.Arrangement.Top -> Arrangement.Top
      Column.Arrangement.Bottom -> Arrangement.Bottom
      Column.Arrangement.Center -> Arrangement.Center
      Column.Arrangement.SpaceEvenly -> Arrangement.SpaceEvenly
      Column.Arrangement.SpaceBetween -> Arrangement.SpaceBetween
      Column.Arrangement.SpaceAround -> Arrangement.SpaceAround
      else -> error("Unknown arrangement")
   }
}

private fun Column.Alignment.toComposeAlignment(): Alignment.Horizontal {
   return when (this) {
      Column.Alignment.Start -> Alignment.Start
      Column.Alignment.CenterHorizontally -> Alignment.CenterHorizontally
      Column.Alignment.End -> Alignment.End
      else -> error("Unknown alignment")
   }
}

/////////////////////////////////////////////////////////////////////////////////////

@SuppressLint("ModifierFactoryExtensionFunction")
private fun D9PadModifier.toComposeModifier(): Modifier {
   return when (this) {
      D9PadModifier -> Modifier

      is CombinedModifier -> left
         .toComposeModifier()
         .then(right.toComposeModifier())

      is Padding -> Modifier.padding(start.dp, top.dp, end.dp, bottom.dp)

      is Background -> Modifier.background(Color(color))

      is Size -> {
         val widthModifier = when (width) {
            is SizePolicy.Exact -> Modifier.width(width.value.dp)
            is SizePolicy.MatchParent -> Modifier.fillMaxWidth()
            is SizePolicy.WrapContent -> Modifier.wrapContentWidth()
         }
         val heightModifier = when (height) {
            is SizePolicy.Exact -> Modifier.height(height.value.dp)
            is SizePolicy.MatchParent -> Modifier.fillMaxHeight()
            is SizePolicy.WrapContent -> Modifier.wrapContentHeight()
         }
         widthModifier.then(heightModifier)
      }
   }
}

/////////////////////////////////////////////////////////////////////////////////////
