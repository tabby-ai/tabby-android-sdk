package ai.tabby.demoappdi.ui

import ai.tabby.android.data.TabbyPayment
import ai.tabby.demoappdi.createSuccessfulPayment
import ai.tabby.demoappdi.ui.theme.TabbyAppTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressScreen(
    tabbyPayment: TabbyPayment
) {
    TabbyAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CartWidget(tabbyPayment = tabbyPayment)
                Spacer(modifier = Modifier.height(18.dp))
                SessionProgressIndicator()
            }
        }
    }
}

@Composable
fun SessionProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.height(40.dp).width(40.dp)
    )
}

@Preview(name = "Light mode",
    showBackground = true
)
@Preview(name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ProgressPreview() {
    ProgressScreen(createSuccessfulPayment())
}
