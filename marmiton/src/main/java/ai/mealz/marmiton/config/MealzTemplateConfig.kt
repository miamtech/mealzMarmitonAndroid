package ai.mealz.marmiton.config

import ai.mealz.marmiton.config.mealzTemplates.EmptyTemplate
import ai.mealz.marmiton.config.mealzTemplates.recipeDetail.RecipeDetailInfo
import ai.mealz.sdk.components.MiamTheme
import ai.mealz.sdk.components.MiamTheme.recipeDetail
import ai.mealz.sdk.theme.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
                    gapBetweenProducts = 4
                }
            }
        }
    }

    private fun overrideIcon() {

    }

    private fun overrideColors()
    {
        Colors.primary = Color(0xFFFF6F61);
    }
}