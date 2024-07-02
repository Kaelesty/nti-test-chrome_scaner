package com.kaelesty.server.di

import com.kaelesty.server.data.connection.ConnectionRepoImpl
import com.kaelesty.server.data.connection.ServerImpl
import com.kaelesty.server.data.scanner.ScannerRepoImpl
import com.kaelesty.server.domain.connection.ConnectionRepo
import com.kaelesty.server.domain.connection.Server
import com.kaelesty.server.domain.scanner.ScannerRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ServerModule {

	@Provides
	fun bindServer(): Server = ServerImpl
}