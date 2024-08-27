package ai.mealz.marmiton.config.mealzTemplates.myBasket.MyMeal

import ai.mealz.core.helpers.formatPrice
import ai.mealz.core.localisation.Localisation
import ai.mealz.sdk.components.myMeal.myMealRecipeCard.success.MyMealRecipeCardSuccess
import ai.mealz.sdk.components.myMeal.myMealRecipeCard.success.MyMealRecipeCardSuccessParameters
import ai.mealz.sdk.theme.Colors
import ai.mealz.sdk.theme.Typography
import ai.mealz.sdk.theme.Dimension
import ai.mealz.sdk.ressource.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


class MarmitonMyMealRecipeCard: MyMealRecipeCardSuccess {

    @Composable
    override fun Content(params: MyMealRecipeCardSuccessParameters) {
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = Colors.disabledText,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                Modifier.padding(8.dp)
            )
            {
                RecipeImage(imageUrl = params.recipe.attributes?.mediaUrl ?: "", params.guestCount, params.openRecipeDetail)
                Column(
                    Modifier.padding(start = 8.dp)
                ) {
                    Row {
                        Text(
                            text = params.recipe.attributes?.title ?: "",
                            style = Typography.subtitleBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(130.dp)
                                .clickable { params.openRecipeDetail() },
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        DeleteButton(params.isDeleting, params.delete)
                    }
                    Text(
                        text = Localisation.myMeals.products(params.numberOfProductsInRecipe).localised,
                        style = Typography.bodySmall,
                        color = Colors.disabledText
                    )
                    TotalPrice(price = params.totalPrice)
                    PricePerPerson(
                        params.totalPrice,
                        params.guestCount
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = params.openRecipeDetail,
                    ) {
                        Row {
                            Text(
                                text = Localisation.recipe.showBasketPreview.localised,
                                style = Typography.subtitle,
                                color = Colors.primary
                            )
                            Icon(
                                painter = painterResource(Image.previous),
                                contentDescription = "Icon arrow view products",
                                modifier = Modifier
                                    .size(22.dp)
                                    .graphicsLayer {
                                        translationY = 4.dp.toPx()
                                        rotationZ = 180f
                                    }
                                    .padding(top = 4.dp),
                                tint = Colors.primary
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    internal fun BadgeViewGuest(numberOfGuests: Int, modifier: Modifier = Modifier) {
        Surface(
            modifier = modifier,
        ) {
            Row(modifier = Modifier.padding(horizontal = 4.dp)) {
                Text(
                    numberOfGuests.toString(),
                    style = Typography.bodyBold,
                )
                Icon(
                    painter = painterResource(id = Image.guests),
                    contentDescription = "guests icon",
                    Modifier
                        .size(24.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }

    @Composable
    internal fun RecipeImage(imageUrl: String, numberOfGuests: Int, action: () -> Unit = {}) {
        Box(
            modifier = Modifier.clickable {
                action()
            }
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .height(Dimension.myMealPictureCardSize)
                    .width(Dimension.myMealPictureCardSize)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            BadgeViewGuest(
                numberOfGuests = numberOfGuests,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp, end = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Colors.white)
            )
        }
    }

    @Composable
    internal fun DeleteButton(isDeleting: Boolean, delete: () -> Unit) {
        IconButton(onClick = delete) {
            if (isDeleting) {
                CircularProgressIndicator(color = Colors.boldText, modifier = Modifier.size(16.dp))
            } else {
                Image(
                    painter = painterResource(Image.delete),
                    contentDescription = "Delete",
                    colorFilter = ColorFilter.tint(Colors.grey),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }

    @Composable
    internal fun PricePerPerson(price: Double, numberOfGuests: Int) {
        val pricePerPerson = if (numberOfGuests != 0) price / numberOfGuests else 0.0
        val formattedPrice = pricePerPerson.formatPrice()
        Text(
            text = Localisation.myMeals.perPerson(formattedPrice).localised,
            style = Typography.bodySmall,
            textAlign = TextAlign.Left,
            color = Colors.grey
        )
    }

    @Composable
    internal fun TotalPrice(price: Double) {
        Text(
            text = price.formatPrice(),
            style = Typography.subtitleBold,
            textAlign = TextAlign.Left,
            color = Colors.grey
        )
    }
}