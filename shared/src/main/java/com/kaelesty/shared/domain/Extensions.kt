package com.kaelesty.shared.domain

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.bytesToMb(): Float {
	return this.toFloat() / 1024 / 1024
}

fun Long.millisToDate(pattern: String = "HH:mm dd-MM-yyyy"): String {
	val formatter = DateTimeFormatter.ofPattern(pattern)
	val date = LocalDateTime.ofInstant(
		Instant.ofEpochMilli(this),
		ZoneId.systemDefault()
	)
	return formatter.format(date)
}