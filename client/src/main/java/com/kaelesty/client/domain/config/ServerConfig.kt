package com.kaelesty.client.domain.config

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
	val ip: String,
	val port: String
)