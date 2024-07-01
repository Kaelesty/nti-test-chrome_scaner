package com.kaelesty.server.di

import com.kaelesty.server.data.connection.ConnectionRepoImpl
import com.kaelesty.server.data.scanner.ScannerRepoImpl
import com.kaelesty.server.domain.connection.ConnectionRepo
import com.kaelesty.server.domain.scanner.ScannerRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ReposModule {

	@Binds
	fun bindScanRepo(impl: ScannerRepoImpl): ScannerRepo

	@Binds
	fun bindConnectionRepo(impl: ConnectionRepoImpl): ConnectionRepo
}