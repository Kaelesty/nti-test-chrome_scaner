package com.kaelesty.client.domain.config

import kotlinx.coroutines.flow.Flow

interface ServerConfigRepo {

	suspend fun saveServerConfig(config: ServerConfig)

	fun getServerConfig(): Flow<ServerConfig?>

}