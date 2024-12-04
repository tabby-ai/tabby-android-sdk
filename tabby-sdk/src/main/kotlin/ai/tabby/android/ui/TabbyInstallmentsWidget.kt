package ai.tabby.android.ui

import ai.tabby.android.R
import ai.tabby.android.data.Currency
import ai.tabby.android.internal.utils.TabbyLanguageResolver
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.math.BigDecimal
import java.math.RoundingMode

class TabbyInstallmentsWidget @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(ctx, attributeSet, defStyleAttr, defStyleRes) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.installments_widget, this)
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
        val divider = BigDecimal(4)
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

        val fullStr = resources.getString(R.string.installments_widget__amount, formattedPayment, currencyStr)
        listOf(R.id.q1amount, R.id.q2amount, R.id.q3amount, R.id.q4amount).forEach {
            findViewById<AppCompatTextView>(it).text = fullStr
        }
        requestLayout()
    }
}