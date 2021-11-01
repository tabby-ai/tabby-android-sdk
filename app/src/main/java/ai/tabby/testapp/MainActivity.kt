package ai.tabby.testapp

import ai.tabby.android.TabbyFactory
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Session
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ai.tabby.testapp.ui.theme.TabbySDKTheme
import android.content.res.Configuration
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    TabbySDKTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StartSessionButton(onClick = viewModel::startSession)
                SessionStatus(status = viewModel.screenStateFlow.value.sessionStatus)
            }
        }
    }
}

@Composable
fun StartSessionButton(onClick: () -> Unit) {
        Button(
            onClick = onClick
        ) {
            Text(
                text = "Start session"
            )
        }
}

@Composable
fun SessionStatus(status: State.SessionStatus) {
    Text(text = status.text)
}

@Preview(name = "Light mode",
    showBackground = true
)
@Preview(name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DefaultPreview() {
    TabbySDKTheme {
        StartSessionButton {}
    }
}

data class State(
    val sessionStatus: SessionStatus,
    val session: Session? = null,
) {
    enum class SessionStatus(
        val text: String
    ) {
        UNKNOWN("Unknown"),
        SUCCESSFUL("Successful"),
        FAILED("FAILED")
    }

    companion object {
        fun default() = State(
            sessionStatus = SessionStatus.UNKNOWN,
            session = null
        )
    }
}

class MainViewModel : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(State.default())
    val screenStateFlow: StateFlow<State> by ::mutableStateFlow

    fun startSession() {
        viewModelScope.launch {
            val result = runCatching {
                TabbyFactory.tabby.createSession(
                    merchantCode = "ae",
                    lang = Lang.EN,
                    payment = createDefaultPayment()
                )
            }
            result.getOrNull()?.let {
                onSessionSuccess(it)
            } ?: onSessionFailed(result.exceptionOrNull())
        }
    }

    private fun onSessionSuccess(s: Session) {
        mutableStateFlow.value = State(
            sessionStatus = State.SessionStatus.SUCCESSFUL,
            session = s
        )
    }

    private fun onSessionFailed(t: Throwable?) {
        mutableStateFlow.value = State(
            sessionStatus = State.SessionStatus.FAILED,
            session = null
        )
    }

}