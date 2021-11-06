package ai.tabby.android.data

import ai.tabby.android.internal.ui.CheckoutActivity
import android.os.Parcelable
import androidx.activity.result.ActivityResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class TabbyResult(

//    val session: Session,

    val result: Result

) : Parcelable {

    enum class Result {
        AUTHORIZED,
        REJECTED,
        CLOSED,
        EXPIRED,
    }
}

val ActivityResult.tabbyResult: TabbyResult? get() =
    data?.getParcelableExtra(CheckoutActivity.EXTRA_TABBY_RESULT)
