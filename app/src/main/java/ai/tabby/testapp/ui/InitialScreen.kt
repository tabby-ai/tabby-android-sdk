package ai.tabby.testapp.ui

import ai.tabby.testapp.MainViewModel
import ai.tabby.testapp.ScreenState
import ai.tabby.testapp.ui.theme.TabbySDKTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InitialScreen(viewModel: MainViewModel) {
    val state = viewModel.screenStateFlow.collectAsState()
    TabbySDKTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StartSessionButton(state, onClick = viewModel::startSession)
                Spacer(modifier = Modifier.height(12.dp))
                SessionStatus(state)
            }
        }
    }
}

@Composable
fun StartSessionButton(state: State<ScreenState>, onClick: () -> Unit) {
    Button(
        enabled = state.value.sessionStatus != ScreenState.SessionStatus.CREATING,
        onClick = onClick
    ) {
        Text(
            text = "Start session",
            style = MaterialTheme.typography.button,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SessionStatus(state: State<ScreenState>) {
    Text(text = state.value.sessionStatus.text)
}
