package ai.tabby.android.ui

import ai.tabby.android.R
import ai.tabby.android.data.Currency
import ai.tabby.android.internal.analytics.api.segmentAnalytics
import ai.tabby.android.internal.ui.SimpleWebActivity
import ai.tabby.android.internal.utils.TabbyLanguageResolver
import ai.tabby.android.ui.analytics.LearnMoreClicked
import ai.tabby.android.ui.analytics.SnippetCardRendered
import android.content.Context
import android.net.Uri
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnLayout
import java.math.BigDecimal
import java.math.RoundingMode

class TabbySnippetWidget @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(ctx, attributeSet, defStyleAttr, defStyleRes) {

    companion object {
        private const val installmentsCount: Int = 4
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.snippet_widget, this)
        view.background =
            ResourcesCompat.getDrawable(resources, R.drawable.snippet_border, context.theme)
        view.setOnClickListener {
            val urlStr = Uri.parse(resources.getString(R.string.snippet_widget__url))
                .buildUpon()
                .appendQueryParameter("price", amount.toString())
                .appendQueryParameter("currency", currency.name)
                .appendQueryParameter("source", "sdk")
                .build()
                .toString()

            SimpleWebActivity.createIntent(
                context,
                urlStr,
                installmentsCount,
                currency.name
            ).let {
                context.startActivity(it)
            }

            segmentAnalytics.sendEvent(LearnMoreClicked(installmentsCount, currency.name))
        }

        doOnLayout {
            segmentAnalytics.sendEvent(SnippetCardRendered(installmentsCount, currency.name))
        }
    }

    var amount: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = value
            update()
        }

    var currency: Currency = Currency.AED
        set(value) {
            field = value
            update()
        }

    private fun update() {
        val divider = BigDecimal(installmentsCount)
        val payment = amount.divide(divider, 2, RoundingMode.CEILING)
        val formattedPayment = String.format(TabbyLanguageResolver.getNumberLocale(), "%.2f", payment)

        val currencyStrId = when (currency) {
            Currency.AED -> R.string.widget__currency__aed
            Currency.SAR -> R.string.widget__currency__sar
            Currency.BHD -> R.string.widget__currency__bhd
            Currency.KWD -> R.string.widget__currency__kwd
            Currency.EGP -> R.string.widget__currency__egp
        }
        val currencyStr = context.getString(currencyStrId)

        val fullStr = resources.getString(R.string.snippet_widget__title, formattedPayment, currencyStr)
        findViewById<AppCompatTextView>(R.id.widgetTitle).text = Html.fromHtml(fullStr)
        requestLayout()
    }
}