package ai.mealz.marmiton.config.components.webview

import ai.mealz.core.Mealz
import ai.mealz.core.di.MealzDI
import ai.mealz.core.services.Analytics
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ShowChangeEvent(
    val message: String,
    val value: Boolean
)

@Serializable
data class PosIdChangeEvent(
    val message: String,
    val posId: String?,
    val posExtId: String?,
    val supplierId: String?,
    val posName: String?,

    val supplierName: String?
)

class MealzStoreLocatorWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AbstractComposeView(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    var webview: WebView? = null
    var onShowChange: (() -> Unit)? = null
    var onSelectStore: ((String) -> Unit)? = null
    var onRequestPermission: (() -> Unit)? = null
    var urlToLoad: String? = null

    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { it ->
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
                                MyJavaScriptInterface(onShowChange = onShowChange, onSelectStore = onSelectStore),
                                "Mealz"
                            )

                            this.webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    // Once the page finishes loading, fetch the location
                                    handleLocationPermission(context)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    // ------------------------ LIFECYCLE TO CHECK RESULT OF PERMISSION UPDATE ---------------------
    fun attachToLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        recheckLocationPermission()
    }

    private fun recheckLocationPermission(): Boolean {
        if (PermissionHelper.hasLocationPermission(context)) {
            fetchUserLocation(context)
            return true
        }
        return false
    }

    // Function to search Mealz API based on coordinates (calls JS function)
    @JavascriptInterface
    fun searchFromCoords(webView: WebView?, latitude: String, longitude: String) {
        val jsCode = "searchBasedOnGeoLocation('$latitude', '$longitude');"
        webView?.post {
            webView.evaluateJavascript(jsCode) { _ -> }
        }
    }

    private fun handleLocationPermission(context: Context) {
        if (!recheckLocationPermission()) {
            onRequestPermission?.invoke() ?: run {
                Toast.makeText(context, "Activity context is required to request permissions.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // If location permission granted, will get user coordinates & launch search
    @SuppressLint("MissingPermission")
    fun fetchUserLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null)
                    searchFromCoords(webview, location.latitude.toString(), location.longitude.toString())
                else
                    Toast.makeText(context, "Failed to fetch location.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error fetching location: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    class MyJavaScriptInterface(var onShowChange: (() -> Unit)?, var onSelectStore: ((String) -> Unit)?) {

        @JavascriptInterface
        fun postMessage(reciveMessage: String) {
            try {
                val data = Json.decodeFromString<ShowChangeEvent>(reciveMessage)
                if (data.message == "showChange" && !data.value) {
                    this.onShowChange?.let { it() }
                }
            } catch (e: Exception) {
                try {
                    val data = Json.decodeFromString<PosIdChangeEvent>(reciveMessage)
                    if (data.message == "posIdChange") {
                        data.posId?.let { posId ->
                            Mealz.user.setStoreWithMealzIdWithCallBack(posId) {
                                data.posName?.let { posName ->
                                    MealzDI.analyticsService.sendEvent(
                                        Analytics.EVENT_POS_SELECTED,
                                        "",
                                        Analytics.PlausibleProps(pos_id = posId, pos_name = posName)
                                    )
                                }
                                this.onSelectStore?.let { it(posId) }
                            }
                        }
                        data.supplierId?.let { supplierId ->
                            data.supplierName?.let { supplierName ->
                                Mealz.user.setRetailer(retailerId = supplierId, retailerName = supplierName)
                            }
                        }
                    }


                } catch (e: Exception) {
                    println("Erreur lors de la désérialisation JSON: $e")
                }
            }
        }
    }
}