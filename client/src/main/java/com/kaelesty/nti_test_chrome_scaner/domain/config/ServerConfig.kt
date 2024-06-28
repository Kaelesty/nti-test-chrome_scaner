package com.kaelesty.nti_test_chrome_scaner.domain.config

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
	val ip: String,
	val port: String
)