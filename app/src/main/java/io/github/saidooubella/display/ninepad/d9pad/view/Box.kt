package io.github.saidooubella.display.ninepad.d9pad.view

import io.github.saidooubella.display.ninepad.d9pad.utils.ChildrenScopeBlock
import io.github.saidooubella.display.ninepad.d9pad.utils.apply
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Modifier

class Box<out Event>(
   val contentAlignment: Alignment = Alignment.TopStart,
   val modifier: Modifier = Modifier,
   children: ChildrenScopeBlock<Event>,
) : View<Event> {

   val children = children.apply()

   @JvmInline
   value class Alignment private constructor(private val value: Int) {
      companion object {
         val TopStart = Alignment(0)
         val TopCenter = Alignment(1)
         val TopEnd = Alignment(2)
         val CenterStart = Alignment(3)
         val Center = Alignment(4)
         val CenterEnd = Alignment(5)
         val BottomStart = Alignment(6)
         val BottomCenter = Alignment(7)
         val BottomEnd = Alignment(8)
      }
   }
}
