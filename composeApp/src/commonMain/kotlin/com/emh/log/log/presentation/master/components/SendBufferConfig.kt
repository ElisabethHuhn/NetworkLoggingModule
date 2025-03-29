package com.emh.log.log.presentation.master.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emh.log.log.presentation.LogAction
import com.emh.log.log.presentation.LogState

@Composable
fun SendBufferConfig(
    state: LogState,
    onAction: (LogAction) -> Unit
) {
    Spacer(modifier = Modifier.height(40.dp))

    OutlinedTextField(
        value = if (state.bufferDuration > 0) state.bufferDuration.toString() else "",
        onValueChange = { enteredDuration: String ->
            onAction(LogAction.UpdateLogBufferDuration(enteredDuration))
        },
        label = {
            Text(
                text = "Max Log Buffer Duration MS",
                style = MaterialTheme.typography.h6,
            )
        }
    )

    Spacer(modifier = Modifier.height(15.dp))
    OutlinedTextField(
        value = if (state.bufferLength == 0) "" else state.bufferLength.toString(),
        onValueChange = { enteredLength: String ->
            onAction(LogAction.UpdateLogBufferLength(enteredLength))
        },
        label = {
            Text(
                text = "Max Log Buffer Length",
                style = MaterialTheme.typography.h6,
            )
        }
    )
}

