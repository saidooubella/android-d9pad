package io.github.saidooubella.display.ninepad.d9pad.view

class Button<out Event>(val value: String, val onClick: Event) : View<Event>
