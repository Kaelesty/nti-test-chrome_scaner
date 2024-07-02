package com.kaelesty.nti_test_chrome_scaner.presentation.memoryusage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaelesty.shared.domain.MemoryUsage
import com.kaelesty.shared.domain.bytesToMb

@Composable
fun MemoryUsageCard() {

	val viewModel = hiltViewModel<MemoryUsageViewModel>()
	val state by viewModel.getMemoryUsage().collectAsState(MemoryUsage(0, 0))

	Card(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Text(text = "Server memory usage (Used/Available)")
			Row(
				Modifier.fillMaxWidth()
			) {
				Text(text = "${
					"%.2f".format(state.usedMemory.bytesToMb())
				} / ${
					"%.2f".format(state.availableMemory.bytesToMb())
				} MB")
			}
		}

	}
}