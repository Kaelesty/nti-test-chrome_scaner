package com.kaelesty.nti_test_chrome_scaner.domain.config

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ServerConfigRepo {

	suspend fun saveServerConfig(config: ServerConfig)

	fun getServerConfig(): Flow<ServerConfig?>

}