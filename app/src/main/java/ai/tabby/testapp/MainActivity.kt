package ai.tabby.testapp

import ai.tabby.testapp.ui.InitialScreen
import ai.tabby.testapp.ui.ProductScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.screenStateFlow.collectAsState()
            when (state.value.sessionStatus) {
                ScreenState.SessionStatus.SUCCESSFUL ->
                    ProductScreen(viewModel = viewModel)
                else -> InitialScreen(viewModel = viewModel)
            }
        }
    }
}


@Preview(name = "Light mode",
    showBackground = true
)
//@Preview(name = "Dark mode",
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true
//)
@Composable
fun DefaultPreview() {
    InitialScreen(viewModel = MainViewModel())
}
