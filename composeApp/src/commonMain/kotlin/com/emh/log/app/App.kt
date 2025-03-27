package com.emh.log.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.emh.log.log.presentation.LoggerScreenRoot
import com.emh.log.log.presentation.LoggerViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = koinViewModel<LoggerViewModel>()
        LoggerScreenRoot(
            viewModel = viewModel,
            onBackClick = {}
        )
    }
}

//@Composable
//private fun OrigionalCode() {
//    var showContent by remember { mutableStateOf(false) }
//    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Button(onClick = { showContent = !showContent }) {
//            Text("Click me!")
//        }
//        AnimatedVisibility(showContent) {
//            val greeting = remember { Greeting().greet() }
//            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                Image(painterResource(Res.drawable.compose_multiplatform), null)
//                Text("Compose: $greeting")
//            }
//        }
//    }
//}