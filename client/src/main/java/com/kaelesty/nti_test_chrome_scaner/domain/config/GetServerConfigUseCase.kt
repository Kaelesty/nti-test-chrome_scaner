package com.kaelesty.nti_test_chrome_scaner.domain.config

import javax.inject.Inject

class GetServerConfigUseCase @Inject constructor(
	private val repo: ServerConfigRepo
) {

	operator fun invoke() = repo.getServerConfig()
}