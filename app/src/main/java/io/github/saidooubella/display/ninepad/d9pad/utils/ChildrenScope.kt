package io.github.saidooubella.display.ninepad.d9pad.utils

import io.github.saidooubella.display.ninepad.d9pad.view.View

typealias ChildrenScopeBlock<Event> = ChildrenScope<Event>.() -> Unit

class ChildrenScope<Event> {
   private val children = mutableListOf<View<Event>>()
   operator fun View<Event>.unaryPlus() = children.plusAssign(this)
   fun build(): List<View<Event>> = children.toList()
}

fun <Event> ChildrenScopeBlock<Event>.apply() = ChildrenScope<Event>().apply(this).build()
