package com.kaelesty.server.data

import kotlinx.coroutines.flow.Flow

interface Connection {

	fun getStateStream(): Flow<String>

	suspend fun sendAction(action: String)

	suspend fun close()
}