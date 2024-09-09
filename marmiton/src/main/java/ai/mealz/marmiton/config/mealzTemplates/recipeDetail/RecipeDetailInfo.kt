package ai.mealz.marmiton.config.mealzTemplates.recipeDetail

import ai.mealz.core.viewModels.storeLocatorButton.StoreLocatorButtonViewModel
import ai.mealz.marmiton.config.components.counter.MiddleText
import ai.mealz.marmiton.config.components.counter.Minus
import ai.mealz.marmiton.config.components.counter.Plus
import ai.mealz.sdk.components.recipeDetail.success.info.RecipeDetailInfo
import ai.mealz.sdk.components.recipeDetail.success.info.RecipeDetailInfoParameters
import ai.mealz.sdk.components.storeLocatorButton.StoreLocatorButton
import ai.mealz.sdk.theme.Colors
import ai.mealz.sdk.theme.Dimension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class RecipeDetailInfo : RecipeDetailInfo {
    @Composable
    override fun Content(params: RecipeDetailInfoParameters) {
        val storeLocatorButton = remember { StoreLocatorButtonViewModel() }

        Column {
            var localCount by remember(params.recipe.id ) { mutableStateOf(params.guestCount) }

            fun newValueBounded(newValue: Int): Boolean {
                return newValue in 0..99
            }

            fun changedValue(localCount: Int?, delta: Int): Int? {
                // if min value is not defined 1 is surely bounded
                if (localCount == null) return 1

                if (!newValueBounded(localCount + delta)) return null

                return localCount + delta
            }

            fun increase() {
                changedValue(localCount, 1)?.let { newCount ->
                    localCount = newCount
                    params.updateGuest(newCount)
                }
            }

            fun decrease() {
                changedValue(localCount, -1)?.let { newCount ->
                    localCount = newCount
                    params.updateGuest(newCount)
                }
            }

            StoreLocatorButton.View(viewModel = storeLocatorButton)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = Dimension.sPadding,
                        vertical = Dimension.sPadding
                    )
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Colors.border), RoundedCornerShape(8.dp))
                ,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier.fillMaxHeight()){

                        Minus({ decrease() },  params.isUpdating)
                        Divider(Modifier.height(50.dp)  //fill the max height
                            .width(1.dp))
                    }

                    MiddleText(localCount,  params.isUpdating)
                    Row(
                        Modifier.fillMaxHeight()){
                        Divider(
                            modifier =  Modifier.height(50.dp)
                                .width(1.dp))
                        Plus({ increase() }, params.isUpdating)
                    }
                }
            }
        }
    }
}