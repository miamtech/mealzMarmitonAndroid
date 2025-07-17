package ai.mealz.marmiton.config

import ai.mealz.marmiton.R
import ai.mealz.marmiton.config.mealzTemplates.DefaultCounter
import ai.mealz.marmiton.config.mealzTemplates.EmptyTemplate
import ai.mealz.marmiton.config.mealzTemplates.myBasket.MarmitonMyBasketEmpty
import ai.mealz.marmiton.config.mealzTemplates.myBasket.MyMeal.MarmitonMyMealRecipeCard
import ai.mealz.marmiton.config.mealzTemplates.recipeDetail.MarmitonRecipeDetailFooter
import ai.mealz.marmiton.config.mealzTemplates.recipeDetail.RecipeDetailInfo
import ai.mealz.sdk.components.MiamTheme
import ai.mealz.sdk.components.MiamTheme.myBasket
import ai.mealz.sdk.components.MiamTheme.myMeal
import ai.mealz.sdk.components.MiamTheme.myProducts
import ai.mealz.sdk.components.MiamTheme.recipeDetail
import ai.mealz.sdk.components.baseComponent.segmentedButton.SegmentedButtonOption
import ai.mealz.sdk.components.baseComponent.segmentedButton.SegmentedButtonRow
import ai.mealz.sdk.components.baseComponent.segmentedButton.SegmentedButtonRowImp
import ai.mealz.sdk.ressource.Image
import ai.mealz.sdk.theme.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color


class MiamTemplateManager {
    init {
        overrideIcon()
        overrideColors()
        MiamTheme.Template {
            recipeDetail {
                success {
                    header {
                        view = EmptyTemplate()
                    }
                    info {
                        view = RecipeDetailInfo()
                    }
                    tag {
                        view = EmptyTemplate()
                    }
                    sponsorBanner {
                        view = EmptyTemplate()
                    }
                    segmentedButtonRow {
                        view = EmptyTemplate()
                    }
                    products {
                        counter {
                            view = DefaultCounter()
                        }
                    }
                    footer { view = MarmitonRecipeDetailFooter() }
                    gapBetweenProducts = 16
                }
            }
            myBasket {
                segmentedButtonRow {
                    view = object : SegmentedButtonRow {
                        @Composable
                        override fun Content(
                            selectedItemIndex: Int,
                            options: List<SegmentedButtonOption>,
                            onSegmentedButtonSelected: (Int) -> Unit
                        ) {
                            LaunchedEffect(Unit) { onSegmentedButtonSelected(1) }
                            SegmentedButtonRowImp().Content(
                                selectedItemIndex = selectedItemIndex,
                                options = options,
                                onSegmentedButtonSelected = onSegmentedButtonSelected
                            )
                        }

                    }
                }
            }
            myMeal {
                empty { view = MarmitonMyBasketEmpty() }
                recipeCard { success { view = MarmitonMyMealRecipeCard() } }
            }
            myProducts {
                empty { view = MarmitonMyBasketEmpty() }
            }
        }
    }

    private fun overrideIcon() {
        Image.cart = R.drawable.ic_add_basket
    }

    private fun overrideColors() {
        Colors.primary = Color(0xFFFF6F61);
        Colors.backgroundLightGrey = Color(0XFFFEF0EE)
    }
}