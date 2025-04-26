package io.github.saidooubella.display.ninepad.d9pad.subscription

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun time(
   formatter: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault()),
): Flow<String> = timer(1000).map { formatter.format(Date()) }
