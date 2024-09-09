package ai.mealz.marmiton.config.components.webview

import ai.mealz.core.localisation.Localisation
import ai.mealz.sdk.components.transferBasket.success.webview.TransferBasketWebviewSuccess
import ai.mealz.sdk.components.transferBasket.success.webview.TransferBasketWebviewSuccessParameters
import android.view.ViewGroup.LayoutParams
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView


class MarmitonBasketTransferWebview: TransferBasketWebviewSuccess {
    @Composable
    override fun Content(params: TransferBasketWebviewSuccessParameters) {
        Column {
            Box(modifier = Modifier
                .height(height = 50.dp)
                .fillMaxWidth()
                .background(color = Color.White)) {

                Button(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = { params.onClose() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(Localisation.basket.basketTranserClose.localised, color = Color.White)
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(factory = {
                    WebView(it).apply {
                        this.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest
                            ): Boolean {
                                view.loadUrl(request.url.toString())
                                return false
                            }
                        }
                        this.settings.javaScriptEnabled = true
                        this.settings.allowFileAccessFromFileURLs = true
                        this.settings.allowUniversalAccessFromFileURLs = true
                        this.settings.allowFileAccess = true
                        this.settings.allowContentAccess = true
                        this.settings.setDomStorageEnabled(true);
                        layoutParams = LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT
                        )
                        this.loadUrl(params.url)
                    }
                })
            }
        }
    }
}
