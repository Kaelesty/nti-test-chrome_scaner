package com.kaelesty.shared.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientAction {


}

@Serializable
sealed interface ServerAction {

	@Serializable
	data class UpdateMemoryUsage(
		val memoryUsage: MemoryUsage
	): ServerAction

}