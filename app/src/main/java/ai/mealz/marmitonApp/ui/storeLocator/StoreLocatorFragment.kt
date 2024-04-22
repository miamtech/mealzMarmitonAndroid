package ai.mealz.marmitonApp.ui.storeLocator

import ai.mealz.core.Mealz
import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentStoreLocatorBinding
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.Fragment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class StoreLocatorFragment() : Fragment() {

    private var _binding: FragmentStoreLocatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("JavascriptInterface")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreLocatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val myWebView: WebView = root.findViewById(R.id.webview)
        val webViewSettings = myWebView.settings;
        webViewSettings.javaScriptEnabled = true
        webViewSettings.allowFileAccessFromFileURLs = true
        webViewSettings.allowUniversalAccessFromFileURLs = true
        webViewSettings.allowFileAccess = true
        webViewSettings.allowContentAccess = true
        onSelectStore = {
            println(it)
        }
        myWebView.addJavascriptInterface(MyJavaScriptInterface(), "Mealz")
        myWebView.loadUrl("file:///android_asset/index.html");
        
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var onSelectStore: ((String) -> Unit)? = null
    }
}

class MyJavaScriptInterface() {
    @JavascriptInterface
    fun postMessage(reciveMessage: String) {
        Log.d("WebView", "Message reçu depuis la WebView : $reciveMessage")
        try {
            val data = Json.decodeFromString<Map<String, String>>(reciveMessage)
            val message = data["message"]
            val value = data["value"]

            if (message == "posIdChange") {
                if (value != null) {
                    Mealz.user.setStoreId(value)
                }
            }
        }catch (e: Exception) {
            println("Erreur lors de la désérialisation JSON: $e")
        }
    }
}
