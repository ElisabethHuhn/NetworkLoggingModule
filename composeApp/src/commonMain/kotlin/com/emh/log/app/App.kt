package com.emh.log.app


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emh.log.log.presentation.master.LogMasterScreenRoot
import com.emh.log.log.presentation.LogViewModel
import com.emh.log.log.presentation.detail.LoggerDetailScreenRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val viewModel = koinViewModel<LogViewModel>()

        NavHost(
            navController = navController,
            startDestination = LogScreen.LOG_MASTER.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)

        ) {
            composable(route = LogScreen.LOG_MASTER.name) {
                LogMasterScreenRoot(
                    viewModel = viewModel,
                    onBackClick = {navController.navigateUp()},
                    navigateToDetail = {navController.navigate(LogScreen.LOG_DETAIL.name)},
                )
            }

            composable(LogScreen.LOG_DETAIL.name) {
                LoggerDetailScreenRoot(
                    viewModel = viewModel,
                    onBackClick = {navController.navigateUp()},
                    navigateToMaster = {navController.navigate(LogScreen.LOG_MASTER.name)}
                )
            }
        }
    }
}


//@Composable
//private fun OriginalCode() {
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