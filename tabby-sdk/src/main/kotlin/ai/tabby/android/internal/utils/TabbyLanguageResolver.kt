package ai.tabby.android.internal.utils

import android.util.LayoutDirection
import androidx.core.text.layoutDirection
import java.util.Locale

object TabbyLanguageResolver {

    const val EMIRATES_LANGUAGE_TAG = "ar-AE"
    const val BAHRAIN_LANGUAGE_TAG = "ar-BH"
    const val KUWAIT_LANGUAGE_TAG = "ar-KW"
    const val SAUDI_LANGUAGE_TAG = "ar-SA"
    const val EGYPT_LANGUAGE_TAG = "ar-EG"
    const val QATAR_LANGUAGE_TAG = "ar-QA"
    const val ENGLISH_LANGUAGE_TAG = "en-AE"
    const val ARABIC_LANGUAGE_ISO_639_1 = "ar"

    private val supportedLanguages = setOf(Locale.ENGLISH.language, ARABIC_LANGUAGE_ISO_639_1)

    fun getLocale(): Locale {
        return Locale.getDefault()
            .let { locale ->
                if (locale.language in supportedLanguages) {
                    locale
                } else if (Locale.getDefault().layoutDirection == LayoutDirection.RTL) {
                    Locale.forLanguageTag(ARABIC_LANGUAGE_ISO_639_1)
                } else {
                    Locale.ENGLISH
                }
            }
    }

    fun getNumberLocale(): Locale = Locale.ENGLISH
}