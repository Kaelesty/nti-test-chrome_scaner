package com.kaelesty.nti_test_chrome_scaner.domain.scanlist

import javax.inject.Inject

class GetScanListUseCase @Inject constructor(
	private val repo: ScanListRepo
) {

	operator fun invoke() = repo.getScanList()
}