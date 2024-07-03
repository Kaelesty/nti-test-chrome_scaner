package com.kaelesty.nti_test_chrome_scaner.presentation.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kaelesty.nti_test_chrome_scaner.R

@Composable
fun StartScanningDialog(
	onAccept: (Int) -> Unit,
	onReject: () -> Unit,
	initialValue: Int = 600
) {

	var seconds by rememberSaveable {
		mutableStateOf(initialValue.toString())
	}

	var isError by rememberSaveable {
		mutableStateOf(false)
	}

	Dialog(onDismissRequest = { onReject() }) {
		Card {
			Column(
				modifier = Modifier.padding(12.dp)
			) {
				Text(text = stringResource(R.string.start_scanning), fontSize = 24.sp)
				Spacer(modifier = Modifier.height(12.dp))
				TextField(
					value = seconds, onValueChange = { seconds = it; isError = false },
					supportingText = { Text(stringResource(R.string.scanning_delay)) }, isError = isError,
					modifier = Modifier.fillMaxWidth(), singleLine = true,
				)
				Button(onClick = {
					try {
						val secondsValue = seconds.toInt()
						if (secondsValue < 0) throw IllegalArgumentException()
						onAccept(secondsValue)
						onReject()
					}
					catch (e: Exception) {
						isError = true
					}
				}, Modifier.fillMaxWidth()) {
					Text(stringResource(R.string.start))
				}
			}
		}
	}
}