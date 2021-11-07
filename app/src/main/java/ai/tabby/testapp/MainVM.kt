package ai.tabby.testapp

import ai.tabby.android.core.TabbyFactory
import ai.tabby.android.data.Lang
import ai.tabby.android.data.Product
import ai.tabby.android.data.TabbySession
import ai.tabby.android.data.TabbyResult
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(ScreenState.default())
    val screenStateFlow: StateFlow<ScreenState> by ::mutableStateFlow

    fun startSession() {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.INITIAL,
            sessionStatus = ScreenState.SessionStatus.CREATING
        )
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

    internal fun onSessionSucceeded(s: TabbySession) {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.PRODUCT,
            sessionStatus = ScreenState.SessionStatus.SUCCESSFUL,
            session = s
        )
    }

    private fun onSessionFailed(t: Throwable?) {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.INITIAL,
            sessionStatus = ScreenState.SessionStatus.FAILED,
        )
    }

    fun createCheckoutIntent(product: Product): Intent =
        TabbyFactory.tabby.createCheckoutIntent(product = product)

    fun onCheckoutResult(result: TabbyResult) {
        mutableStateFlow.value = mutableStateFlow.value.copy(
            state = ScreenState.State.CHECKOUT_RESULT,
            checkoutResult = result
        )
    }

    fun resetToInitialState() {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.INITIAL,
            sessionStatus = ScreenState.SessionStatus.UNKNOWN,
        )
    }
}

data class ScreenState(
    val state: State,
    val sessionStatus: SessionStatus,
    val session: TabbySession? = null,
    val checkoutResult: TabbyResult? = null
) {
    enum class State {
        INITIAL, // Create session button is displayed
        PRODUCT, // Product selection buttons are displayed
        CHECKOUT_RESULT, // Checkout result is displayed along with Restart button
    }

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
            state = State.INITIAL,
            sessionStatus = SessionStatus.UNKNOWN,
        )
    }
}
