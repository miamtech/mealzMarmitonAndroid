package ai.mealz.marmiton.config.mealzTemplates.recipeDetail

import ai.mealz.core.di.MealzDI
import ai.mealz.core.services.Analytics
import ai.mealz.core.services.Analytics.Companion.EVENT_BASKET_PREVIEW
import ai.mealz.core.viewModels.dynamicRecipeDetailFooter.IngredientStatusTypes
import ai.mealz.sdk.components.recipeDetail.success.footer.RecipeDetailSuccessFooter
import ai.mealz.sdk.components.recipeDetail.success.footer.RecipeDetailSuccessFooterImp
import ai.mealz.sdk.components.recipeDetail.success.footer.RecipeDetailSuccessFooterParameters
import androidx.compose.runtime.Composable

class MarmitonRecipeDetailFooter: RecipeDetailSuccessFooter {
    @Composable
    override fun Content(params: RecipeDetailSuccessFooterParameters) {
        RecipeDetailSuccessFooterImp().Content(RecipeDetailSuccessFooterParameters(
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
}