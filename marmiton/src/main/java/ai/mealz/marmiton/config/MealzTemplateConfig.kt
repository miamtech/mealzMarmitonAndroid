package ai.mealz.marmiton.config

import ai.mealz.marmiton.R
import ai.mealz.marmiton.config.components.webview.MarmitonBasketTransferWebview
import ai.mealz.marmiton.config.mealzTemplates.DefaultCounter
import ai.mealz.marmiton.config.mealzTemplates.EmptyTemplate
import ai.mealz.marmiton.config.mealzTemplates.myBasket.MarmitonMyBasketEmpty
import ai.mealz.marmiton.config.mealzTemplates.recipeDetail.MarmitonRecipeDetailFooter
import ai.mealz.marmiton.config.mealzTemplates.recipeDetail.RecipeDetailInfo
import ai.mealz.sdk.components.MiamTheme
import ai.mealz.sdk.components.MiamTheme.recipeDetail
import ai.mealz.sdk.components.MiamTheme.myBasket
import ai.mealz.sdk.components.MiamTheme.myMeal
import ai.mealz.sdk.components.MiamTheme.myProducts
import ai.mealz.sdk.components.MiamTheme.transferBasket
import ai.mealz.sdk.ressource.Image
import ai.mealz.sdk.theme.Colors
import androidx.compose.ui.graphics.Color


class MiamTemplateManager {
    init {
        overrideIcon()
        overrideColors()
        MiamTheme.Template {
            recipeDetail{
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
                    sponsorBanner{
                        view = EmptyTemplate()
                    }
                    swapper {
                        view = EmptyTemplate()
                    }
                    products {
                        counter  {
                            view =  DefaultCounter()
                        }
                    }
                    footer { view = MarmitonRecipeDetailFooter() }
                    gapBetweenProducts = 16
                }
            }
            myBasket {
                transferBasket {
                    success {
                        webview { view = MarmitonBasketTransferWebview() }
                    }
                }
            }
            myMeal {
                empty { view = MarmitonMyBasketEmpty() }
            }
            myProducts {
                empty { view = MarmitonMyBasketEmpty() }
            }
        }
    }

    private fun overrideIcon() {
        Image.cart = R.drawable.ic_add_basket
    }

    private fun overrideColors()
    {
        Colors.primary = Color(0xFFFF6F61);
        Colors.backgroundLightGrey = Color(0XFFFEF0EE)
    }
}