package ai.tabby.android.data

import ai.tabby.android.internal.ui.CheckoutActivity
import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.parcelize.Parcelize

/**
 * Tabby checkout result, which is passed as activity result to the calling activity when Tabby
 * Checkout activity is finished.
 *
 * If your app uses [ActivityResultContracts] you can do the following in your activity:
 *
 *       ```
 *       private val checkoutContract =
 *           registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
 *               when (result.resultCode) {
 *                   ComponentActivity.RESULT_OK -> {
 *                       result.tabbyResult?.let { tabbyResult ->
 *                           viewModel.onCheckoutResult(tabbyResult)
 *                       } ?: // Tabby result is null
 *                   }
 *                   else -> {
 *                      // Result is not OK
 *                   }
 *               }
 *           }
 *       ```
 * @see ActivityResult.tabbyResult
 * @see Intent.tabbyResult
*/
@Parcelize
data class TabbyResult(

    val result: Result

) : Parcelable {

    enum class Result {
        /**
         * Customer is authorized by Tabby checkout.
         */
        AUTHORIZED,

        /**
         * Customer is rejected by Tabby checkout.
         */
        REJECTED,

        /**
         * Customer has closed Tabby checkout activity.
         */
        CLOSED,

        /**
         * Tabby session was expired. You need to call
         * [createSession][ai.tabby.android.core.Tabby.createSession] again
         * to get new [TabbySession].
         */
        EXPIRED,
    }
}

/**
 * An extension that allows to get the [TabbyResult] from [ActivityResult]
 */
val ActivityResult.tabbyResult: TabbyResult? get() = data?.tabbyResult

/**
 * An extension that allows to get the [TabbyResult] from [Intent]
 */
val Intent.tabbyResult: TabbyResult? get() =
    getParcelableExtra(CheckoutActivity.EXTRA_TABBY_RESULT)
