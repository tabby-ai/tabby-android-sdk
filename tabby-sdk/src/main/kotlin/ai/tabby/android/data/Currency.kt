package ai.tabby.android.data

/**
 * @param decimals number of minor-unit decimal places (ISO 4217) — 3 for the dinar currencies
 * (BHD, KWD), 2 for the rest.
 */
enum class Currency(val decimals: Int) {
    AED(2),
    SAR(2),
    BHD(3),
    KWD(3),
    EGP(2)
}