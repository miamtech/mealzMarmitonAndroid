package ai.mealz.marmiton.config.components.webview
import ai.mealz.core.Mealz
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
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

class MealzWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AbstractComposeView(context, attrs, defStyleAttr) {

    var webview : WebView? = null
    var onSelectStore: ((String) -> Unit)? = null

    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.settings.javaScriptEnabled = true
                    this.settings.allowFileAccessFromFileURLs = true
                    this.settings.allowUniversalAccessFromFileURLs = true
                    this.settings.allowFileAccess = true
                    this.settings.allowContentAccess = true

                    webview = this
                }
                return@AndroidView webview!!
            }, update = {
                it.loadUrl("file:///android_asset/index.html");
                webview?.addJavascriptInterface(MyJavaScriptInterface(onSelectStore = onSelectStore), "Mealz")

            })
        }
    }
}

class MyJavaScriptInterface (onSelectStore: ((String) -> Unit)?) {
    var onSelectStore: ((String) -> Unit)? = onSelectStore
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