package ai.mealz.marmiton.config.mealzTemplates.myBasket

import ai.mealz.sdk.components.baseComponent.emptyPage.EmptyPage
import ai.mealz.sdk.components.baseComponent.emptyPage.EmptyPageParameters
import ai.mealz.sdk.theme.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MarmitonMyBasketEmpty: EmptyPage {
    @Composable
    override fun Content(params: EmptyPageParameters) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(ai.mealz.sdk.ressource.Image.search),
                    contentDescription = "magnifying glass",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer {
                            scaleX = -1f /* Flip the image across the Y axis */
                        }
                        .padding(top = 16.dp, bottom = 24.dp),
                    colorFilter = ColorFilter.tint(Colors.primary)
                )
                Text(
                    text = params.title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}