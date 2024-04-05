package ai.mealz.marmiton.config.mealzTemplates.recipeDetail

import ai.mealz.sdk.components.baseComponent.counter.CounterImp
import ai.mealz.sdk.components.baseComponent.counter.CounterParameters
import ai.mealz.sdk.components.recipeDetail.success.info.RecipeDetailInfo
import ai.mealz.sdk.components.recipeDetail.success.info.RecipeDetailInfoParameters
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

class RecipeDetailInfo : RecipeDetailInfo {
    @Composable
    override fun Content(params: RecipeDetailInfoParameters) {
        Column {
            CounterImp().Content(
                CounterParameters(
                    initialCount = params.guestCount,
                    onCounterChanged = params.updateGuest
                )
            )
        }
    }
}