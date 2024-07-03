package com.kaelesty.client.presentation.scanlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaelesty.nti_test_chrome_scaner.R
import com.kaelesty.shared.domain.Scan
import com.kaelesty.shared.domain.bytesToMb
import com.kaelesty.shared.domain.millisToDate

@Composable
fun ScanListContent() {

	val viewModel = hiltViewModel<ScanListViewModel>()
	val state by viewModel.getScanList().collectAsState()

	LazyColumn(
		modifier = Modifier
			.padding(horizontal = 16.dp)
	) {
		items(state) {
			Spacer(modifier = Modifier.height(4.dp))
			ScanCard(
				scan = it,
				onRestoreScan = {
					viewModel.restoreScan(it)
				}
			)
		}
	}
}

@Composable
fun ScanCard(
	scan: Scan,
	onRestoreScan: ((Int) -> Unit)?
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Row(
			modifier = Modifier
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				modifier = Modifier.weight(1f)
			) {
				with(scan) {
					Text(
						text = "Scan #${id}",
						fontWeight = FontWeight.Bold,
						fontSize = 18.sp,
					)
					Spacer(modifier = Modifier.height(4.dp))
					Text(
						text = "Started at: ${meta.startTime.millisToDate()}",
						fontSize = 16.sp,
					)
					Text(
						text = "Time taken: ${(meta.scanTime / 1000).toInt()} sec",
						fontSize = 16.sp,
					)
					Text(
						text = "Files size: ${"%.2f".format(meta.allFilesSize.bytesToMb())} MB",
						fontSize = 16.sp,
					)
				}
			}
			onRestoreScan?.let {
				Button(
					onClick = { it(scan.id) },
				) {
					Text(stringResource(R.string.restore))
				}
			}
		}
	}
}