package io.github.saidooubella.display.ninepad.d9pad.view

class Field<out Event>(
   val value: String,
   val onValueChange: (String) -> Event,
) : View<Event>
