package io.github.saidooubella.display.ninepad.d9pad.view.modifier

class Padding(val start: Int, val top: Int, val end: Int, val bottom: Int) : Modifier

fun Padding(vertical: Int = 0, horizontal: Int = 0) = Padding(horizontal, vertical, horizontal, vertical)

fun Padding(size: Int) = Padding(size, size, size, size)
