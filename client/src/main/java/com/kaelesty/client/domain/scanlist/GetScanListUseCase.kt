package com.kaelesty.client.domain.scanlist

import javax.inject.Inject

class GetScanListUseCase @Inject constructor(
	private val repo: ScanListRepo
) {

	operator fun invoke() = repo.getScanList()
}