package ai.tabby.testapp

import ai.tabby.android.TabbyFactory
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Product
import ai.tabby.android.data.Session
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(ScreenState.default())
    val screenStateFlow: StateFlow<ScreenState> by ::mutableStateFlow

    fun startSession() {
        mutableStateFlow.value = ScreenState(sessionStatus = ScreenState.SessionStatus.CREATING)
        viewModelScope.launch {
            val result = runCatching {
                TabbyFactory.tabby.createSession(
                    merchantCode = "ae",
                    lang = Lang.EN,
                    payment = createDefaultPayment()
                )
            }
            result.getOrNull()?.let {
                onSessionSucceeded(it)
            } ?: onSessionFailed(result.exceptionOrNull())
        }
    }

    internal fun onSessionSucceeded(s: Session) {
        mutableStateFlow.value = ScreenState(
            sessionStatus = ScreenState.SessionStatus.SUCCESSFUL,
            session = s
        )
    }

    private fun onSessionFailed(t: Throwable?) {
        mutableStateFlow.value = ScreenState(
            sessionStatus = ScreenState.SessionStatus.FAILED,
            session = null
        )
    }

    fun startProduct(product: Product) {

    }
}

data class ScreenState(
    val sessionStatus: SessionStatus,
    val session: Session? = null,
) {
    enum class SessionStatus(
        val text: String
    ) {
        UNKNOWN("Unknown"),
        CREATING("Creating..."),
        SUCCESSFUL("Successful"),
        FAILED("FAILED")
    }

    companion object {
        fun default() = ScreenState(
            sessionStatus = SessionStatus.UNKNOWN,
            session = null
        )
    }
}
