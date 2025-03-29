package com.emh.log.log.presentation.master.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emh.log.log.presentation.LogAction
import com.emh.log.log.presentation.LogState

@Composable
fun SendBufferTriggers(
    state: LogState,
    onAction: (LogAction) -> Unit
) {
    Spacer(modifier = Modifier.height(15.dp))

    OutlinedTextField(
        value = if (state.numberToGenerate > 0) state.numberToGenerate.toString() else "",
        onValueChange = { enteredNumToGen: String -> onAction(
            LogAction.UpdateNumberToGenerateChanged(
                enteredNumToGen
            )
        )
        },
        label = {
            Text(
                text = "Number log messages to generate",
                style = MaterialTheme.typography.h6,
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))


    Button(
        onClick = {
            onAction(LogAction.GenerateLogMessages)
        }
    ) {
        Text(
            text = "Log message(s)",
            style = MaterialTheme.typography.button,
        )
    }

    Button(
        onClick = {
            onAction(LogAction.FlushLogBuffer)
        }
    ) {
        Text(
            text = "Flush Buffer",
            style = MaterialTheme.typography.button,
        )
    }
}
