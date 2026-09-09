package ai.tabby.android.internal.utils

import ai.tabby.android.data.Currency
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Formats an amount as a widget snippet URL price param, scaled to [currency]'s minor unit (2
 * decimals for most currencies, 3 for the dinars). [TabbyPayment.amount][ai.tabby.android.data.TabbyPayment.amount]
 * is a decimal value by design — round to the currency's precision instead of requiring the
 * amount to already be a whole number.
 */
internal fun BigDecimal.toWidgetPriceParam(currency: Currency): String =
    setScale(currency.decimals, RoundingMode.HALF_UP).toPlainString()
