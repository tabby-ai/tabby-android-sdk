package ai.tabby.demoapp.qa.ui

import ai.tabby.android.data.TabbyResult
import androidx.compose.ui.graphics.Color

/**
 * QA-friendly success/cancel/error classification of a raw [TabbyResult], for status chips.
 */
enum class ResultOutcome(val label: String, val color: Color) {
    SUCCESS("Success", Color(0xFF2E7D32)),
    REJECTED("Rejected", Color(0xFFF9A825)),
    CANCELLED("Cancelled", Color(0xFF757575)),
    ERROR("Error", Color(0xFFC62828)),
}

fun TabbyResult.Result?.toOutcome(): ResultOutcome = when (this) {
    TabbyResult.Result.AUTHORIZED -> ResultOutcome.SUCCESS
    TabbyResult.Result.REJECTED -> ResultOutcome.REJECTED
    TabbyResult.Result.CLOSED -> ResultOutcome.CANCELLED
    TabbyResult.Result.EXPIRED, null -> ResultOutcome.ERROR
}
