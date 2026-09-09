package ai.tabby.demoapp.qa.ui

import ai.tabby.android.internal.network.TabbyEnvironment
import ai.tabby.demoapp.qa.DemoConfig
import ai.tabby.demoapp.qa.restartApp
import ai.tabby.demoapp.ui.theme.TabbyAppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private enum class EnvChoice(val label: String) { STAGE("Stage"), PROD("Prod"), CUSTOM("Custom") }

private fun TabbyEnvironment.toChoice() = when (this) {
    is TabbyEnvironment.Stage -> EnvChoice.STAGE
    is TabbyEnvironment.Prod -> EnvChoice.PROD
    is TabbyEnvironment.Custom -> EnvChoice.CUSTOM
}

/**
 * QA-only screen for infra-level SDK setup: which backend environment and API key
 * [ai.tabby.android.factory.TabbyFactory.setup] is initialized with. Since setup only runs once
 * per process, saving here restarts the app instead of hot-swapping the SDK instance.
 */
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    var envChoice by remember { mutableStateOf(DemoConfig.environment.toChoice()) }
    val existingCustom = DemoConfig.environment as? TabbyEnvironment.Custom
    var customBaseUrl by remember { mutableStateOf(existingCustom?.baseUrl.orEmpty()) }
    var customAnalyticsUrl by remember { mutableStateOf(existingCustom?.analyticsUrl.orEmpty()) }
    var apiKey by remember { mutableStateOf(DemoConfig.apiKey) }

    TabbyAppTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text("SDK setup", style = MaterialTheme.typography.h6)
                Text(
                    "Applies on next app restart — TabbyFactory.setup() only runs once per process.",
                    style = MaterialTheme.typography.caption,
                )
                Spacer(Modifier.height(16.dp))

                Text("Environment", fontWeight = FontWeight.Bold)
                EnvChoice.entries.forEach { choice ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { envChoice = choice }
                    ) {
                        RadioButton(selected = envChoice == choice, onClick = { envChoice = choice })
                        Text(choice.label)
                    }
                }

                if (envChoice == EnvChoice.CUSTOM) {
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = customBaseUrl,
                        onValueChange = { customBaseUrl = it },
                        label = { Text("Base URL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customAnalyticsUrl,
                        onValueChange = { customAnalyticsUrl = it },
                        label = { Text("Analytics URL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(Modifier.height(20.dp))
                Text("API key", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = {
                        DemoConfig.environment = when (envChoice) {
                            EnvChoice.STAGE -> TabbyEnvironment.Stage
                            EnvChoice.PROD -> TabbyEnvironment.Prod
                            EnvChoice.CUSTOM -> TabbyEnvironment.Custom(customBaseUrl, customAnalyticsUrl)
                        }
                        DemoConfig.apiKey = apiKey
                        restartApp(context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Save & restart app")
                }
            }
        }
    }
}
