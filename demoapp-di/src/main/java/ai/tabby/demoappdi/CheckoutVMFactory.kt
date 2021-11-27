package ai.tabby.demoappdi

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CheckoutVMFactory(
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm = CheckoutViewModel()
        (app as App).myTabbyComponent.inject(vm)
        return vm as T
    }
}
