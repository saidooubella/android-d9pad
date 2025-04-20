@file:Suppress("NOTHING_TO_INLINE")

package io.github.saidooubella.display.ninepad

import kotlin.reflect.KProperty

class MutableRef<T>(var value: T)

inline operator fun <T> MutableRef<T>.getValue(thisRef: Any?, property: KProperty<*>) = value

inline operator fun <T> MutableRef<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
   this.value = value
}
