package com.kaelesty.server.domain.connection

import kotlinx.coroutines.flow.Flow

interface ConnectionRepo {

	suspend fun saveServerPort(port: String)

	fun getServerPort(): Flow<String?>
}