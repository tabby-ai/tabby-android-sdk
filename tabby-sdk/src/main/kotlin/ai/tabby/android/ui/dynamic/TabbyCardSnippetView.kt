package ai.tabby.android.ui.dynamic

import ai.tabby.android.data.Lang
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

class TabbyCardSnippetView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(ctx, attributeSet, defStyleAttr, defStyleRes) {

    var tabbyPayment: TabbyPayment? = null
        set(value) {
            field = value
            requestLayout()
        }

    var merchantCode: String = ""
        set(value) {
            field = value
            requestLayout()
        }

    var lang: Lang = Lang.EN
        set(value) {
            field = value
            requestLayout()
        }

    var installmentsCount: Int = 4
        set(value) {
            field = value
            requestLayout()
        }

    init {
        val view = ComposeView(ctx).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
            setContent {
                val payment = tabbyPayment ?: return@setContent
                TabbyCardSnippet(
                    tabbyPayment = payment,
                    merchantCode = merchantCode,
                    lang = lang,
                    installmentsCount = installmentsCount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 136.dp),
                )
            }
        }
        addView(view)
    }
}
