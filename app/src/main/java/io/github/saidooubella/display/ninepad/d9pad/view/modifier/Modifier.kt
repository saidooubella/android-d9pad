package io.github.saidooubella.display.ninepad.d9pad.view.modifier

sealed interface Modifier {
   companion object : Modifier
}

operator fun Modifier.plus(other: Modifier): Modifier = CombinedModifier(this, other)

class CombinedModifier(val left: Modifier, val right: Modifier) : Modifier
