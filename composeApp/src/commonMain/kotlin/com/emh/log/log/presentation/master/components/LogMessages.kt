package com.emh.log.log.presentation.master.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emh.log.log.presentation.LogAction
import com.emh.log.log.presentation.LogState

@Composable
fun LogMessages(
    state: LogState,
    onAction: (LogAction) -> Unit,
    navigateToDetail: () -> Unit
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
            onAction(LogAction.FetchLogMessages)
        }
    ) {
        Text(
            text = "Fetch Logged message(s)",
            style = MaterialTheme.typography.button,
        )
    }

    Text(
        text = "Fetched ${state.logMessages.size} messages ",
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = "Last Message ",
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "= ${state.logMessages.lastOrNull()}",
        style = MaterialTheme.typography.body1,
    )

    Button(
        onClick = {
//            onAction(LogAction.OnBackClick)
            navigateToDetail()
        }
    ) {
        Text(
            text = "List Log Messages",
            style = MaterialTheme.typography.button,
        )
    }

}