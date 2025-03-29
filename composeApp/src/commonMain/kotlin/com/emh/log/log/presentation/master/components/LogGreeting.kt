package com.emh.log.log.presentation.master.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emh.log.log.presentation.LogAction
import com.emh.log.log.presentation.LogState

@Composable
fun LogGreeting(
    state: LogState,
    onAction: (LogAction) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))
    Divider(
        color = MaterialTheme.colors.primary,
        thickness = 2.dp,
        startIndent = 0.dp
    )
    Spacer(modifier = Modifier.height(10.dp))
    Button(
        onClick = {
            onAction(LogAction.FetchLogGreeting)
        }
    ) {
        Text(
            text = "Fetch Log Greeting",
            style = MaterialTheme.typography.button,
        )
    }
    Text(
        text = state.logGreeting,
        style = MaterialTheme.typography.body1,
    )
}

