package ai.mealz.marmitonApp.ui.storeLocator

import ai.mealz.marmiton.config.components.webview.MealzStoreLocatorWebView
import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentStoreLocatorBinding
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class StoreLocatorFragment : DialogFragment() {

    private var _binding: FragmentStoreLocatorBinding? = null
    private val binding get() = _binding!!

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
            val mainHandler = context?.mainLooper?.let { Handler(it) }
            mainHandler?.post {
                val navController = findNavController()
                if (navController.currentBackStack.value.isNotEmpty() && navController.currentBackStack.value[navController.currentBackStack.value.size - 2].destination.label == "Home") {
                    navController.popBackStack()
                    navController.navigate(R.id.navigation_recipe_detail)
                } else {
                    navController.popBackStack()
                }
            }
        }
        myWebView.onSelectStore = { _ ->
            val mainHandler = context?.mainLooper?.let { Handler(it) }
            mainHandler?.post {
                val navControler = findNavController()
                if (navControler.currentBackStack.value.isNotEmpty() && navControler.currentBackStack.value[navControler.currentBackStack.value.size - 2].destination.label == "Home") {
                    navControler.popBackStack()
                    navControler.navigate(R.id.navigation_recipe_detail, )
                } else {
                    navControler.popBackStack()
                }
            }
            
        myWebView.onSelectStore = { _ ->
            dismiss()
        }

        // Return the correctly inflated view
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}
