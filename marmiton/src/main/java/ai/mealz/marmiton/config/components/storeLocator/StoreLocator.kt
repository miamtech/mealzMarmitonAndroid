package ai.mealz.marmiton.config.components.storeLocator

import ai.mealz.core.Mealz
import ai.mealz.core.handler.LogHandler
import android.content.Context
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import androidx.compose.runtime.Composable
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class StoreLocator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AbstractComposeView(context, attrs, defStyleAttr) {

    var onSelectStore: (String) -> Unit = { LogHandler.error("onSelectStore is not provided") }

    @Composable
    override fun Content() {
        View { onSelectStore(it)}
    }

    companion object {
        private const val URL = "file:///android_asset/index.html"
        @Composable
        fun View(onSelectStore: ((String) -> Unit)?) {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(factory = {
                    return@AndroidView WebView(it).apply {
                        layoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT
                        )
                        this.settings.javaScriptEnabled = true
                        this.settings.allowFileAccessFromFileURLs = true
                        this.settings.allowUniversalAccessFromFileURLs = true
                        this.settings.allowFileAccess = true
                        this.settings.allowContentAccess = true
                    }
                }, update = {
                    URL.let { it1 -> it.loadUrl(it1) };
                    it.addJavascriptInterface(MyJavaScriptInterface(onSelectStore = onSelectStore), "Mealz")
                })
            }
        }
    }
}

class MyJavaScriptInterface (val onSelectStore: ((String) -> Unit)?) {

    @JavascriptInterface
    fun postMessage(reciveMessage: String) {
        try {
            val data = Json.decodeFromString<Map<String, String>>(reciveMessage)
            val message = data["message"]
            val value = data["value"]

            if (message == "posIdChange") {
                if (value != null) {
                    Mealz.user.setStoreId(value)
                    this.onSelectStore?.let { it(value) }
                }
            }
        }catch (e: Exception) {
            println("Erreur lors de la désérialisation JSON: $e")
        }
    }
}