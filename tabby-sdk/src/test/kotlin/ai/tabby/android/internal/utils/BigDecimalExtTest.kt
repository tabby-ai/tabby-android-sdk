package ai.tabby.android.internal.utils

import ai.tabby.android.data.Currency
import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

class BigDecimalExtTest {

    @Test
    fun `fractional amount is rounded to the currency's minor unit, not rejected`() {
        assertEquals("51.75", BigDecimal("51.75").toWidgetPriceParam(Currency.AED))
        assertEquals("299.90", BigDecimal("299.90").toWidgetPriceParam(Currency.SAR))
    }

    @Test
    fun `three-decimal dinar currencies keep their full precision`() {
        assertEquals("18.500", BigDecimal("18.500").toWidgetPriceParam(Currency.KWD))
        assertEquals("18.500", BigDecimal("18.5").toWidgetPriceParam(Currency.BHD))
    }

    @Test
    fun `whole and zero-fraction amounts still format correctly`() {
        assertEquals("340.00", BigDecimal("340").toWidgetPriceParam(Currency.AED))
        assertEquals("340.00", BigDecimal("340.00").toWidgetPriceParam(Currency.AED))
        assertEquals("340.000", BigDecimal("340").toWidgetPriceParam(Currency.KWD))
    }

    @Test
    fun `amount with more precision than the currency's minor unit is rounded half up`() {
        assertEquals("51.76", BigDecimal("51.755").toWidgetPriceParam(Currency.AED))
        assertEquals("18.501", BigDecimal("18.5005").toWidgetPriceParam(Currency.KWD))
    }
}
