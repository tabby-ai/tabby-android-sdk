package ai.tabby.demoapp.ui

import ai.tabby.demoapp.CheckoutViewModel
import ai.tabby.demoapp.ScreenState
import ai.tabby.demoapp.qa.ui.DebugPayloadView
import ai.tabby.demoapp.qa.ui.toOutcome
import ai.tabby.demoapp.ui.theme.TabbyAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays the outcome of a checkout run — status + the raw [ai.tabby.android.data.TabbyResult]
 * payload — so QA can verify exactly what the SDK returned.
 */
@Composable
fun CheckoutResultScreen(
    viewModel: CheckoutViewModel,
    onDone: () -> Unit
) {
    val state = viewModel.screenStateFlow.collectAsState()
    TabbyAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultStatusChip(state = state)
                Spacer(modifier = Modifier.height(16.dp))
                val result = state.value.checkoutResult
                val error = state.value.errorMessage
                when {
                    result != null -> DebugPayloadView(title = "Raw TabbyResult", payload = result)
                    error != null -> DebugPayloadView(title = "Error", payload = error)
                }
                Spacer(modifier = Modifier.height(20.dp))
                DoneButton(onClick = onDone)
            }
        }
    }
}

@Composable
fun ResultStatusChip(state: State<ScreenState>) {
    val rawResult = state.value.checkoutResult?.result
    val outcome = rawResult.toOutcome()
    val label = if (rawResult != null) "${outcome.label} (${rawResult.name})" else outcome.label
    Surface(
        shape = RoundedCornerShape(50),
        color = outcome.color.copy(alpha = 0.15f),
    ) {
        Text(
            text = label,
            color = outcome.color,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
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
