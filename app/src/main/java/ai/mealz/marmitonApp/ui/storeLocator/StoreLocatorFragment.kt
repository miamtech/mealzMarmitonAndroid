package ai.mealz.marmitonApp.ui.storeLocator

import ai.mealz.marmiton.config.components.webview.MealzStoreLocatorWebView
import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentStoreLocatorBinding
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


class StoreLocatorFragment : DialogFragment() {

    private var _binding: FragmentStoreLocatorBinding? = null
    private val binding get() = _binding!!
    private var hasChanged = false

    @SuppressLint("JavascriptInterface", "RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentStoreLocatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Ensure you reference the correct WebView instance
        val myWebView: MealzStoreLocatorWebView = root.findViewById(R.id.store_locator)
        myWebView.urlToLoad = "file:///android_asset/index.html"
        myWebView.onShowChange = {
            dismiss()
        }

        myWebView.onSelectStore = { _ ->
            hasChanged = true
            dismiss()
        }

        // Return the correctly inflated view
        return root
    }

    override fun onDestroyView() {
        val dialog = requireActivity().supportFragmentManager.findFragmentByTag("dialogRecipe")
        if (!hasChanged && dialog != null) {

            
            (dialog as DialogFragment).dismiss()
        }
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}
