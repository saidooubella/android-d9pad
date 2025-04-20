package io.github.saidooubella.display.ninepad

fun String.insert(char: Char, cursor: Int) = substring(0, cursor) + char + substring(cursor)

fun String.remove(cursor: Int) = substring(0, cursor - 1) + substring(cursor)
