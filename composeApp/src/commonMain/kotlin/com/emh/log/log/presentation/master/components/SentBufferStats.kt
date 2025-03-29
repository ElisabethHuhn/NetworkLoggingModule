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
fun SentBufferStats(state: LogState) {
    Spacer(modifier = Modifier.height(10.dp))
    Divider(
        color = MaterialTheme.colors.primary,
        thickness = 2.dp,
        startIndent = 0.dp
    )
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Buffer Sent Stats: ",
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = "total sent = ${state.totalSent}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = "total queued = ${state.totalQueued}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = "total Success = ${state.totalSuccess}",
        style = MaterialTheme.typography.body1,
    )
    Text(
        text = "total Error = ${state.totalError}",
        style = MaterialTheme.typography.body1,
    )
}
