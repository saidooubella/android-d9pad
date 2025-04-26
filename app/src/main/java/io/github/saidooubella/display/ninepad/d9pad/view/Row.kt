package io.github.saidooubella.display.ninepad.d9pad.view

import io.github.saidooubella.display.ninepad.d9pad.utils.ChildrenScopeBlock
import io.github.saidooubella.display.ninepad.d9pad.utils.apply
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Modifier

class Row<out Event>(
   val horizontalArrangement: Arrangement = Arrangement.Start,
   val verticalAlignment: Alignment = Alignment.Top,
   val modifier: Modifier = Modifier,
   children: ChildrenScopeBlock<Event>,
) : View<Event> {

   val children = children.apply()

   @JvmInline
   value class Arrangement(private val value: Int) {
      companion object {
         val Start = Arrangement(0)
         val End = Arrangement(1)
         val Center = Arrangement(2)
         val SpaceEvenly = Arrangement(3)
         val SpaceBetween = Arrangement(4)
         val SpaceAround = Arrangement(5)
      }
   }

   @JvmInline
   value class Alignment(private val value: Int) {
      companion object {
         val Top = Alignment(0)
         val CenterVertically = Alignment(1)
         val Bottom = Alignment(2)
      }
   }
}
