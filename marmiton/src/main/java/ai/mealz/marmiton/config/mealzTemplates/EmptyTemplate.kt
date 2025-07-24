package ai.mealz.marmiton.config.mealzTemplates

import ai.mealz.sdk.components.baseComponent.segmentedButton.SegmentedButtonOption
import ai.mealz.sdk.components.baseComponent.segmentedButton.SegmentedButtonRow
import ai.mealz.sdk.components.recipeDetail.success.header.RecipeDetailHeader
import ai.mealz.sdk.components.recipeDetail.success.header.RecipeDetailHeaderParameters
import ai.mealz.sdk.components.recipeDetail.success.info.RecipeDetailInfo
import ai.mealz.sdk.components.recipeDetail.success.info.RecipeDetailInfoParameters
import ai.mealz.sdk.components.recipeDetail.success.sponsorBanner.RecipeDetailSponsorBanner
import ai.mealz.sdk.components.recipeDetail.success.sponsorBanner.RecipeDetailSponsorBannerParameters
import ai.mealz.sdk.components.recipeDetail.success.tag.RecipeDetailSuccessTag
import ai.mealz.sdk.components.recipeDetail.success.tag.RecipeDetailSuccessTagParameters
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable

class EmptyTemplate: RecipeDetailHeader, RecipeDetailInfo, RecipeDetailSuccessTag,
    RecipeDetailSponsorBanner, SegmentedButtonRow {
    @Composable
    override fun Content(params: RecipeDetailHeaderParameters) {
        Box {}
    }

    @Composable
    override fun Content(params: RecipeDetailInfoParameters) {
        Box {}
    }

    @Composable
    override fun Content(params: RecipeDetailSuccessTagParameters) {
        Box {}
    }

    @Composable
    override fun Content(params: RecipeDetailSponsorBannerParameters) {
        Box {}
    }

    @Composable
    override fun Content(
        selectedItemIndex: Int,
        options: List<SegmentedButtonOption>,
        onSegmentedButtonSelected: (Int) -> Unit
    ) {
        Box {}
    }
}