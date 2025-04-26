package io.github.saidooubella.display.ninepad.d9pad.view

import io.github.saidooubella.display.ninepad.d9pad.utils.ChildrenScopeBlock
import io.github.saidooubella.display.ninepad.d9pad.utils.apply
import io.github.saidooubella.display.ninepad.d9pad.view.modifier.Modifier

class Column<out Event>(
   val verticalArrangement: Arrangement = Arrangement.Top,
   val horizontalAlignment: Alignment = Alignment.Start,
   val modifier: Modifier = Modifier,
   children: ChildrenScopeBlock<Event>,
) : View<Event> {

   val children = children.apply()

   @JvmInline
   value class Arrangement private constructor(private val value: Int) {
      companion object {
         val Top = Arrangement(0)
         val Bottom = Arrangement(1)
         val Center = Arrangement(2)
         val SpaceEvenly = Arrangement(3)
         val SpaceBetween = Arrangement(4)
         val SpaceAround = Arrangement(5)
      }
   }

   @JvmInline
   value class Alignment private constructor(private val value: Int) {
      companion object {
         val Start = Alignment(0)
         val CenterHorizontally = Alignment(1)
         val End = Alignment(2)
      }
   }
}
