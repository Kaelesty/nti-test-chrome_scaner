package com.kaelesty.client.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun UiLockedDialog() {

	Dialog(onDismissRequest = {  }) {
		Card(
		) {
			Column(
				modifier = Modifier
					.padding(16.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				CircularProgressIndicator(
					modifier = Modifier.size(64.dp)
				)
				Text("The server is in the process of restoring the scan...")
			}
		}
	}
}