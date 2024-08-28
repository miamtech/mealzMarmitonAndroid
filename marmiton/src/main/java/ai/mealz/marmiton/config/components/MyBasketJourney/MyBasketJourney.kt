package ai.mealz.marmiton.config.components.MyBasketJourney

import ai.mealz.core.viewModels.itemSelector.ItemSelectorContract
import ai.mealz.core.viewModels.itemSelector.ItemSelectorViewModel
import ai.mealz.core.viewModels.myBasket.MyBasketViewModel
import ai.mealz.core.viewModels.myMeal.MyMealViewModel
import ai.mealz.sdk.components.itemSelector.ItemSelector
import ai.mealz.sdk.components.myBasket.MyBasket
import ai.mealz.sdk.components.transferBasket.TransferBasket
import android.content.Context
import android.util.AttributeSet
import ai.mealz.core.viewModels.storeLocatorButton.StoreLocatorButtonViewModel
import ai.mealz.sdk.components.storeLocatorButton.StoreLocatorButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.unit.dp
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
        val storeLocatorButton = remember { StoreLocatorButtonViewModel() }

        NavHost(navController = navController, startDestination = "MY_BASKET") {
            composable("MY_BASKET") {
                Column {
                    StoreLocatorButton.View(viewModel = storeLocatorButton)
                    Spacer(modifier = Modifier.height(8.dp))
                    MyBasket.View(
                        myBasketVm = myBasketVm,
                        myMealsVm = myMealsVm,
                        onButtonClickedWhenEmpty = {},
                        onTransferBasketNavigation = { goToTransferBasket(it) },
                        onNavigateToItemSelector = { goToItemSelector() }
                    )
                }
            }
            composable("TRANSFER_BASKET/{url}"){ backStackEntry ->
                backStackEntry.arguments?.let { arguments ->
                    TransferBasket.View(url = arguments.getString("url") ?: "", retailerName = "toto") {
                        navController.popBackStack()
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