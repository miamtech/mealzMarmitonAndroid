package ai.mealz.marmiton.config.components.webview
import ai.mealz.core.Mealz
import ai.mealz.core.di.MealzDI
import ai.mealz.core.services.Analytics
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MealzStoreLocatorWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AbstractComposeView(context, attrs, defStyleAttr) {

    var webview: WebView? = null
    var onSelectStore: ((String) -> Unit)? = null
    var urlToLoad: String? = null
    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = {
                WebView(it).apply {
                    this.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.settings.javaScriptEnabled = true
                    this.settings.allowFileAccessFromFileURLs = true
                    this.settings.allowUniversalAccessFromFileURLs = true
                    this.settings.allowFileAccess = true
                    this.settings.allowContentAccess = true

                    webview = this

                    this.post {
                        run {
                            this.loadUrl(urlToLoad ?: error("Should pass an url in webview"))
                            MealzDI.analyticsService.sendEvent(Analytics.EVENT_PAGEVIEW, "/locator", Analytics.PlausibleProps())
                            this.addJavascriptInterface(
                                MyJavaScriptInterface(onSelectStore = onSelectStore),
                                "Mealz"
                            )
                        }
                    }
                }
            })
        }
    }


    class MyJavaScriptInterface(onSelectStore: ((String) -> Unit)?) {
        var onSelectStore: ((String) -> Unit)? = onSelectStore

        @JavascriptInterface
        fun postMessage(reciveMessage: String) {
            try {
                val data = Json.decodeFromString<Map<String, String>>(reciveMessage)
                val message = data["message"]

                if (message == "posIdChange") {
                    data["posId"]?.let { posId ->
                        Mealz.user.setStoreWithMealzIdWithCallBack(posId) {
                            data["posName"]?.let { posName ->
                                MealzDI.analyticsService.sendEvent(
                                    Analytics.EVENT_POS_SELECTED,
                                    "",
                                    Analytics.PlausibleProps(pos_id = posId, pos_name = posName)
                                )
                            }
                            this.onSelectStore?.let { it(posId) }
                        }
                    }
                }
            } catch (e: Exception) {
                println("Erreur lors de la désérialisation JSON: $e")
            }
        }
    }
}