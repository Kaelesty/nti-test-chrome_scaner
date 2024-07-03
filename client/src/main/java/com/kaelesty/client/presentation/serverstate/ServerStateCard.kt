package com.kaelesty.client.presentation.serverstate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaelesty.shared.domain.bytesToMb

@Composable
fun MemoryUsageCard() {

	val viewModel = hiltViewModel<ServerStateViewModel>()
	val state by viewModel.stateFlow.collectAsState()

	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Column(
			modifier = Modifier.padding(12.dp)
		) {
			Row(
				Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Column {
					Text(text = "Memory usage", fontWeight = FontWeight.Light)
					Text(text = "${
						"%.2f".format(state.memoryUsage.usedMemory.bytesToMb())
					} / ${
						"%.2f".format(state.memoryUsage.availableMemory.bytesToMb())
					} MB")
				}
				VerticalDivider(
					color = Color.Gray,
					modifier = Modifier.height(45.dp)
				)
				Column {
					Text(text = "Server connection:", fontWeight = FontWeight.Light)
					Text(text = state.connectionState.message)
				}
			}
		}

	}
}