package ai.tabby.demoapp

import ai.tabby.android.core.TabbyFactory
import ai.tabby.android.data.*
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(ScreenState.default())
    val screenStateFlow: StateFlow<ScreenState> by ::mutableStateFlow

    fun createSession(tabbyPayment: TabbyPayment) {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.CREATING_SESSION
        )
        viewModelScope.launch {
            val result = runCatching {
                TabbyFactory.tabby.createSession(
                    merchantCode = "ae",
                    lang = Lang.EN,
                    payment = tabbyPayment
                )
            }
            result.getOrNull()?.let { tabbySession ->
                onSessionSucceeded(tabbySession)
            } ?: onSessionFailed(result.exceptionOrNull())
        }
    }

    internal fun onSessionSucceeded(s: TabbySession) {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.SESSION_CREATED,
            session = s
        )
    }

    private fun onSessionFailed(t: Throwable?) {
        mutableStateFlow.value = ScreenState(
            state = ScreenState.State.SESSION_FAILED,
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
        mutableStateFlow.value = ScreenState.default()
    }
}

data class ScreenState(
    val state: State,
    val session: TabbySession? = null,
    val checkoutResult: TabbyResult? = null
) {
    enum class State {
        INITIAL,
        CREATING_SESSION,   // Create session is in progress
        SESSION_CREATED,    // Product selection buttons are displayed
        SESSION_FAILED,     // Retry button is displayed
        CHECKOUT_RESULT,    // Checkout result is displayed along with Done button
    }

    companion object {
        fun default() = ScreenState(
            state = State.INITIAL
        )
    }
}
