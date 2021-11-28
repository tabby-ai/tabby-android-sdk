package ai.tabby.android.internal.ui.screen

import ai.tabby.android.data.TabbyResult
import ai.tabby.android.internal.ui.theme.TabbySdkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun CheckoutTestScreen(onResult: (TabbyResult) -> Unit) {
    TabbySdkTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tabby checkout")
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton(text = "Authorized") { onResult(TabbyResult(TabbyResult.Result.AUTHORIZED))}
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton(text = "Rejected") { onResult(TabbyResult(TabbyResult.Result.REJECTED))}
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton(text = "Closed") { onResult(TabbyResult(TabbyResult.Result.CLOSED))}
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton(text = "Expired") { onResult(TabbyResult(TabbyResult.Result.EXPIRED))}
            }
        }
    }
}

@Composable
internal fun ActionButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            fontSize = 18.sp
        )
    }
}

@Preview(name = "Light mode",
    showBackground = true
)
@Composable
fun TestPreview() {
    CheckoutTestScreen { }
}
