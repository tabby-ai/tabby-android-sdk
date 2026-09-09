package ai.tabby.demoapp.qa

import ai.tabby.android.data.TabbyResult
import ai.tabby.demoapp.qa.ui.PaymentBuilderScreen
import ai.tabby.demoapp.qa.ui.SettingsScreen
import ai.tabby.demoapp.qa.ui.WidgetsShowcaseScreen
import ai.tabby.demoapp.ui.theme.TabbyAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * QA harness launcher. Hosts the payment builder (home), settings, and widgets showcase screens.
 * This whole `qa` package is testing scaffolding — for the actual SDK integration reference see
 * [ai.tabby.demoapp.App], [ai.tabby.demoapp.CheckoutActivity] and [ai.tabby.demoapp.CheckoutViewModel].
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabbyAppTheme {
                var payment by remember { mutableStateOf(PaymentPresets.successful()) }
                var lastResult by remember { mutableStateOf<TabbyResult?>(null) }
                var lastError by remember { mutableStateOf<String?>(null) }
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues()),
                        topBar = {
                            TopAppBar(
                                title = { Text("Tabby Demo — ${DemoConfig.environment.qaLabel()}") },
                            )
                        }
                    ) { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = "payment",
                            modifier = Modifier.padding(padding),
                        ) {
                            composable("payment") {
                                PaymentBuilderScreen(
                                    payment = payment,
                                    onPaymentChange = { payment = it },
                                    lastResult = lastResult,
                                    lastError = lastError,
                                    onCheckoutFinished = { result, error ->
                                        lastResult = result
                                        lastError = error
                                    },
                                    onOpenSettings = { navController.navigate("settings") },
                                    onOpenWidgets = { navController.navigate("widgets") },
                                )
                            }
                            composable("settings") { SettingsScreen() }
                            composable("widgets") { WidgetsShowcaseScreen(payment = payment) }
                        }
                    }
                }
            }
        }
    }
}
