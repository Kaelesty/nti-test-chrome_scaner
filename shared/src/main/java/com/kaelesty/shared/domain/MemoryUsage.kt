package com.kaelesty.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class MemoryUsage(
	val usedMemory: Long = 0,
	val availableMemory: Long = 0
)