package com.kaelesty.client.domain.scanlist

import com.kaelesty.shared.domain.Scan
import kotlinx.coroutines.flow.StateFlow

interface ScanListRepo {

	fun getScanList(): StateFlow<List<Scan>>
}