package ai.mealz.marmiton.config.components.mealzJourney

import ai.mealz.core.viewModels.dynamicRecipeDetail.DynamicRecipeDetailViewModel
import ai.mealz.core.viewModels.itemSelector.ItemSelectorContract
import ai.mealz.core.viewModels.itemSelector.ItemSelectorViewModel
import ai.mealz.sdk.components.itemSelector.ItemSelector
import ai.mealz.sdk.components.recipeDetail.RecipeDetail
import ai.mealz.sdk.components.sponsorDetail.SponsorDetail
import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow

class MealzJourney @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val state = MutableStateFlow<String?>(null)

    private var close = {}
    private var isMealzId = true

    fun bind(recipeId: String? = null, isMealzId: Boolean = true, close: () -> Unit) {
        state.value = recipeId
        this.isMealzId = isMealzId
        this.close = close
    }

    @Composable
    override fun Content() {

        val navController = rememberNavController()

        val recipeId = state.collectAsState()

        fun goToItemSelector(ingredientId: String) =
            navController.navigate("ITEM_SELECTOR/${ingredientId}") { launchSingleTop = true }

        fun goToSponsor(sponsorId: String) =
            navController.navigate("SPONSOR/${sponsorId}") { launchSingleTop = true }

        fun back() {
            navController.popBackStack()
        }

        val dynamicRecipeDetailViewModel = remember {
            DynamicRecipeDetailViewModel({ close() }, ::goToItemSelector)
        }

        val itemSelectorViewModel = remember { ItemSelectorViewModel() }

        recipeId.value?.let { recipeId ->
            NavHost(navController = navController, startDestination = "DETAIL") {
                composable("DETAIL") {
                    Box(
                        modifier = Modifier.padding(16.dp, 24.dp)
                    ) {
                        RecipeDetail.View(
                            viewModel = dynamicRecipeDetailViewModel,
                            recipeId = recipeId,
                            isMealzId,
                            goToSponsor = { goToSponsor(it) },
                            cookOnlyMode = false
                        )
                    }
                }
                composable("ITEM_SELECTOR/{ingredientId}") { backStackEntry ->
                    backStackEntry.arguments?.let { arguments ->
                        ItemSelector.View(
                            itemSelectorViewModel,
                            arguments.getString("ingredientId")
                        ) {
                            itemSelectorViewModel.setEvent(ItemSelectorContract.Event.ReturnToDetail)
                            back()
                        }
                    }

                }
                composable("SPONSOR/{sponsorId}") { backStackEntry ->
                    backStackEntry.arguments?.getString("sponsorId")?.let { sponsorId ->
                        SponsorDetail(LocalContext.current).apply { bind(sponsorId) { back() } }
                            .Content()
                    }
                }
            }
        }
    }
}