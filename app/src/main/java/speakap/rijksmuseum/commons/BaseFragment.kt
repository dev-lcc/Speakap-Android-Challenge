package speakap.rijksmuseum.commons

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.cancelChildren

open class BaseFragment: Fragment() {
    // Top-level Nav Controller Instance
    protected val appNavController: NavController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher
            ?.addCallback(
                owner = this@BaseFragment,
                enabled = enableBackPressedCallback(),
                onBackPressed = onBackPressedCallback()
            )
    }

    /**
     * Helper functions to handle on back pressed callback
     * - Override this and set to `true` if you want the implementing Fragment to enable onBackPressedCallback implementation
     */
    open fun enableBackPressedCallback(): Boolean = false

    /**
     * Override this and set to `true` if you want the implementing Fragment to have its own onBackPressedCallback implementation
     */
    open fun onBackPressedCallback(): OnBackPressedCallback.() -> Unit = {}

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel Coroutine Flow Subscriptions launched on `lifecycleScope`
        lifecycleScope.coroutineContext.cancelChildren()
    }

    /**
     * Log TAG
     */
    protected val TAG = this::class.java.simpleName
}
