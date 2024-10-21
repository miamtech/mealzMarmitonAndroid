package ai.mealz.marmiton.config.mealzTemplates.recipeDetail

import ai.mealz.core.base.state.ComponentUiState
import ai.mealz.core.di.MealzDI
import ai.mealz.core.localisation.Localisation
import ai.mealz.core.services.Analytics
import ai.mealz.core.services.Analytics.Companion.EVENT_BASKET_PREVIEW
import ai.mealz.core.viewModels.dynamicRecipeDetailFooter.IngredientStatusTypes
import ai.mealz.sdk.components.price.formatPrice
import ai.mealz.sdk.components.recipeDetail.success.footer.RecipeDetailSuccessFooter
import ai.mealz.sdk.components.recipeDetail.success.footer.RecipeDetailSuccessFooterParameters
import ai.mealz.sdk.ressource.Image.cart
import ai.mealz.sdk.theme.Colors.primary
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MarmitonRecipeDetailFooter: RecipeDetailSuccessFooter {
    @Composable
    override fun Content(params: RecipeDetailSuccessFooterParameters) {
        View(RecipeDetailSuccessFooterParameters(
            price = params.price,
            priceOfProductsInBasket = params.priceOfProductsInBasket,
            priceOfRemainingProducts = params.priceOfRemainingProducts,
            priceProductsInBasketPerGuest = params.priceProductsInBasketPerGuest,
            priceStatus = params.priceStatus,
            ingredientsStatus = params.ingredientsStatus,
            isButtonLock = params.isButtonLock,
            onConfirm = {
                if (params.ingredientsStatus.type == IngredientStatusTypes.NO_MORE_TO_ADD) {
                    MealzDI.analyticsService.sendEvent(EVENT_BASKET_PREVIEW, "", Analytics.PlausibleProps())
                }
                params.onConfirm()
            }
        ))
    }

    @Composable
    fun View(params: RecipeDetailSuccessFooterParameters) {

        val priceOfProductsInBasket = params.priceOfProductsInBasket.collectAsState()
        val priceOfRemainingProducts = params.priceOfRemainingProducts.collectAsState()

        val isButtonLock = params.isButtonLock.collectAsState()
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(Modifier.weight(1f)) {
                when (params.priceStatus) {
                    ComponentUiState.EMPTY, ComponentUiState.IDLE -> {
                        Box {} // show nothing until price is loaded
                    }
                    ComponentUiState.SUCCESS, ComponentUiState.LOADING -> Column {
                        if (params.priceStatus == ComponentUiState.LOADING) {
                            Box(Modifier.size(16.dp)) {
                                CircularProgressIndicator(color = primary)
                            }
                        }
                        if (params.priceStatus != ComponentUiState.LOADING && priceOfProductsInBasket.value > 0) {
                            Text(
                                text = priceOfProductsInBasket.value.formatPrice(),
                                style = TextStyle(fontSize = 16.sp, color = ai.mealz.sdk.theme.Colors.primary, fontWeight = FontWeight.Black)
                            )
                            Text(
                                text = Localisation.recipeDetails.inMyBasket.localised,
                                style = TextStyle(fontSize = 10.sp, color = ai.mealz.sdk.theme.Colors.grey)
                            )
                        }
                    }
                    else -> {}
                }
            }
            if (isButtonLock.value) {
                LoadingButton()
            } else {
                when (params.ingredientsStatus.type) {
                    IngredientStatusTypes.NO_MORE_TO_ADD -> ContinueButton(text = Localisation.recipeDetails.continueShopping.localised) { params.onConfirm() }
                    IngredientStatusTypes.REMAINING_INGREDIENTS_TO_BE_ADDED, IngredientStatusTypes.INITIAL_STATE -> {
                        AddButton(text =
                        "${Localisation.ingredient.addProduct(params.ingredientsStatus.count).localised} (${priceOfRemainingProducts.value.formatPrice()})"
                        ) { params.onConfirm() }
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingButton() {
        Surface(shape = RoundedCornerShape(10.dp), color = ai.mealz.sdk.theme.Colors.primary) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(Modifier.size(20.dp), ai.mealz.sdk.theme.Colors.white)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun AddButton(text: String, action: () -> Unit = {}) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = ai.mealz.sdk.theme.Colors.primary,
            onClick = { action() }) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(cart), contentDescription = "$cart")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = text, style = TextStyle(fontSize = 16.sp, color = ai.mealz.sdk.theme.Colors.white, fontWeight = FontWeight.Black))
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ContinueButton(text: String, action: () -> Unit = {}) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, ai.mealz.sdk.theme.Colors.white),
            color = ai.mealz.sdk.theme.Colors.primary,
            onClick = { action() }) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = text, style = TextStyle(fontSize = 16.sp, color = ai.mealz.sdk.theme.Colors.white, fontWeight = FontWeight.Black))
            }
        }
    }
}