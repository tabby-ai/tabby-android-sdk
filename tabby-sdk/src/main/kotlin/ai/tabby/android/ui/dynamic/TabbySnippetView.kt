package ai.tabby.android.ui.dynamic

import ai.tabby.android.data.TabbyPayment
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp

class TabbySnippetView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(ctx, attributeSet, defStyleAttr, defStyleRes) {

    var tabbyPayment: TabbyPayment? = null
        set(value) {
            field = value
            requestLayout()
        }

    init {
        val view = ComposeView(ctx).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
            setContent {
                val payment = tabbyPayment ?: return@setContent
                TabbySnippet(
                    tabbyPayment = payment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 50.dp)
                )
            }
        }
        addView(view)
    }

}