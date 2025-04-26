package io.github.saidooubella.display.ninepad.d9pad.view.modifier

class Size(
   val width: SizePolicy = SizePolicy.WrapContent,
   val height: SizePolicy = SizePolicy.WrapContent,
) : Modifier

sealed interface SizePolicy {
   object MatchParent : SizePolicy
   object WrapContent : SizePolicy
   class Exact(val value: Int) : SizePolicy
}
