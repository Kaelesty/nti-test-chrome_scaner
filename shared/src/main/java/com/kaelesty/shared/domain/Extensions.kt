package com.kaelesty.shared.domain

fun Long.bytesToMb(): Float {
	return this.toFloat() / 1024 / 1024
}