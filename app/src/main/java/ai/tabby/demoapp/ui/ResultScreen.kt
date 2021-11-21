package ai.tabby.demoapp.ui

import ai.tabby.android.data.TabbyResult
import ai.tabby.demoapp.CheckoutViewModel
import ai.tabby.demoapp.ScreenState
import ai.tabby.demoapp.ui.theme.TabbyAppTheme
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
fun CheckoutResultScreen(
    viewModel: CheckoutViewModel,
    onDone: () -> Unit
) {
    val state = viewModel.screenStateFlow.collectAsState()
    TabbyAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultStatusText(state = state)
                Spacer(modifier = Modifier.height(12.dp))
                DoneButton(onClick = onDone)
            }
        }
    }
}

@Composable
fun ResultStatusText(state: State<ScreenState>) {
    Text(
        text = when (state.value.checkoutResult?.result) {
            TabbyResult.Result.AUTHORIZED -> "Authorized"
            TabbyResult.Result.REJECTED -> "Rejected"
            TabbyResult.Result.CLOSED -> "just closed"
            TabbyResult.Result.EXPIRED -> "session is expired"
            else -> "UNKNOWN RESULT"
        },
        fontSize = 20.sp
    )
}

@Composable
fun DoneButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(
            text = "Done",
            style = MaterialTheme.typography.button,
            fontSize = 18.sp
        )
    }
}
