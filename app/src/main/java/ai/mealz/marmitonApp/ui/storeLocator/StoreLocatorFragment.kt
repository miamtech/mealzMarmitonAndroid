package ai.mealz.marmitonApp.ui.storeLocator

import ai.mealz.core.Mealz
import ai.mealz.marmitonApp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ai.mealz.marmitonApp.databinding.FragmentStoreLocatorBinding
import android.annotation.SuppressLint
import android.widget.Button
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject

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
        root.findViewById<Button>(R.id.button_store_A)
            .setOnClickListener {
                Mealz.user.setStoreId("25910")
            }
        root.findViewById<Button>(R.id.button_store_B)
            .setOnClickListener {
                Mealz.user.setStoreId("25910")
            }

        val myWebView: WebView = root.findViewById(R.id.webview)
        val webViewSettings = myWebView.settings;
        webViewSettings.javaScriptEnabled = true
        webViewSettings.allowFileAccessFromFileURLs = true
        webViewSettings.allowUniversalAccessFromFileURLs = true
        webViewSettings.allowFileAccess = true
        webViewSettings.allowContentAccess = true
        Companion.onSelectStore?.let { JsObjcet(it) }?.let { myWebView.addJavascriptInterface(it, "Mealz") }
        myWebView.loadUrl("file:///android_asset/index.html");

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var onSelectStore: ((String) -> Void)? = null
    }
}

class JsObjcet(var onSelectStore: (String) -> Void) {
    @JavascriptInterface
    fun postMessage(message: String) {
        println(message)
        val storeId = JSONObject(message).get("id") as String
        Mealz.user.setStoreId(storeId)
        onSelectStore(storeId)
    }
}