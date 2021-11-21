package ai.tabby.demoapp.ui

import ai.tabby.demoapp.ui.theme.TabbyAppTheme
import android.content.res.Configuration
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
fun FailedScreen(
    onRetry: () -> Unit
) {
    TabbyAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error: Can't create session",
                    style = MaterialTheme.typography.button,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(onClick = onRetry) {
                    Text(
                        text = "Retry",
                        style = MaterialTheme.typography.button,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}


@Preview(name = "Light mode",
    showBackground = true
)
@Preview(name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun FailedPreview() {
    FailedScreen() {}
}
