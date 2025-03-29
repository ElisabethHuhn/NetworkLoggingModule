package com.emh.log.log.presentation.master

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emh.log.log.presentation.LogAction
import com.emh.log.log.presentation.LogState
import com.emh.log.log.presentation.LogViewModel
import com.emh.log.log.presentation.master.components.LastErrorStats
import com.emh.log.log.presentation.master.components.LastMessages
import com.emh.log.log.presentation.master.components.LogGreeting
import com.emh.log.log.presentation.master.components.LogMessages
import com.emh.log.log.presentation.master.components.SendBufferConfig
import com.emh.log.log.presentation.master.components.SendBufferTriggers
import com.emh.log.log.presentation.master.components.SentBufferStats


@Composable
fun LogMasterScreenRoot(
    viewModel: LogViewModel,
    onBackClick: () -> Unit,
    navigateToDetail: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LogMasterScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is LogAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        navigateToDetail
    )
}

@Composable
fun LogMasterScreen(
    state: LogState,
    onAction: (LogAction) -> Unit,
    navigateToDetail: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SendBufferConfig(state = state, onAction = onAction)

        SendBufferTriggers(state = state, onAction = onAction)

        LastMessages(state)

        SentBufferStats(state)

        LastErrorStats(state)

        LogGreeting(state = state, onAction = onAction)

        LogMessages(state = state, onAction = onAction, navigateToDetail = navigateToDetail)
    }
}















