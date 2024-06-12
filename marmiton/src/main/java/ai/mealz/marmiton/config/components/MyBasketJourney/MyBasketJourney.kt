package ai.mealz.marmiton.config.components.MyBasketJourney

import ai.mealz.core.viewModels.itemSelector.ItemSelectorContract
import ai.mealz.core.viewModels.itemSelector.ItemSelectorViewModel
import ai.mealz.core.viewModels.myBasket.MyBasketViewModel
import ai.mealz.core.viewModels.myMeal.MyMealViewModel
import ai.mealz.core.viewModels.myProducts.MyProductsViewModel
import ai.mealz.sdk.components.itemSelector.ItemSelector
import ai.mealz.sdk.components.myBasket.MyBasket
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class MyBasketJourney @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AbstractComposeView(context, attrs, defStyleAttr) {

    private val myBasketVm = MyBasketViewModel()
    private val myMealsVm = MyMealViewModel()
    private val itemSelectorViewModel = ItemSelectorViewModel()

    @Composable
    override fun Content() {
        val navController = rememberNavController()
        fun goToTransferBasket(url: String) {
            val encodedUrl: String = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            navController.navigate("TRANSFER_BASKET/${encodedUrl}") { launchSingleTop = true }
        }
        fun goToItemSelector() {
            navController.navigate("ITEM_SELECTOR") { launchSingleTop = true }
        }

        NavHost(navController = navController, startDestination = "MY_BASKET") {
            composable("MY_BASKET") {
              MyBasket.View(
                  myBasketVm = myBasketVm,
                  myMealsVm = myMealsVm,
                  onButtonClickedWhenEmpty = {},
                  onTransferBasketNavigation = {
                      goToTransferBasket(it)
                  },
                  onNavigateToItemSelector = { goToItemSelector() })
            }
            composable("TRANSFER_BASKET/{url}"){ backStackEntry ->
                backStackEntry.arguments?.let { arguments ->
                    Column {
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
                                    this.loadUrl(arguments.getString("url") ?: "")
                                }
                            })
                        }
                    }
                }
            }
            composable("ITEM_SELECTOR") { backStackEntry ->
                ItemSelector.View(
                    itemSelectorViewModel,
                    null
                ) {
                    itemSelectorViewModel.setEvent(ItemSelectorContract.Event.ReturnToDetail)
                    navController.navigate("MY_BASKET") { launchSingleTop = false }
                }
            }
        }
    }
}