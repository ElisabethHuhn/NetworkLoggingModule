package com.emh.log.log.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emh.log.log.presentation.LogAction
import com.emh.log.log.presentation.LogState
import com.emh.log.log.presentation.LogViewModel

@Composable
fun LoggerDetailScreenRoot(
    viewModel: LogViewModel,
    onBackClick: () -> Unit,
    navigateToMaster : () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoggerDetailScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is LogAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateToMaster = { navigateToMaster() }
    )
}

@Composable
fun LoggerDetailScreen(
    state: LogState,
    onAction: (LogAction) -> Unit,
    navigateToMaster: () -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Text(
                text = "Log Messages",
                style = MaterialTheme.typography.h5,
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navigateToMaster() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Master Screen")
            }

        }
        if (state.logMessages.isEmpty()) {
            item {
                Text(
                    text = "No log messages available",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            items(state.logMessages.size) { index ->
                val logEntry = state.logMessages[index]
                Text(
                    text = "severity = ${logEntry.severity}",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "timestamp = ${logEntry.timestamp}",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = logEntry.message,
                    style = MaterialTheme.typography.body1,
                )

                Spacer(modifier = Modifier.height(4.dp))
                Divider()
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navigateToMaster() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Master Screen")
                }

            }
        }
    }
}













