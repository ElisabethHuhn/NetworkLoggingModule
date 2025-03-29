package com.emh.log.log.presentation.master.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emh.log.log.presentation.LogState

@Composable
fun LastMessages(state: LogState) {
    Spacer(modifier = Modifier.height(10.dp))
    Divider(
        color = MaterialTheme.colors.primary,
        thickness = 2.dp,
        startIndent = 0.dp
    )
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Last Log Entry Created: ",
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = "severity = ${state.lastBuffered.severity}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = "Timestamp = ${state.lastBuffered.timestamp}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = state.lastBuffered.message,
        style = MaterialTheme.typography.body1,
    )
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Last Log Entry Sent: ",
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = "severity = ${state.lastSent.severity}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = "Timestamp = ${state.lastSent.timestamp}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = state.lastSent.message,
        style = MaterialTheme.typography.body1,
    )
}

