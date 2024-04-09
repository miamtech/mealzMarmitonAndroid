package ai.mealz.marmiton.config.components.counter

import ai.mealz.marmiton.R
import ai.mealz.sdk.ressource.Image
import ai.mealz.sdk.theme.Colors
import ai.mealz.sdk.theme.Dimension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Minus (decrease: () -> Unit, isDisable: Boolean) {
    IconButton(
        onClick = { decrease() },
        enabled = !isDisable,
        modifier = Modifier.clip(RoundedCornerShape(topEnd = 70f, bottomEnd = 70f))

    ) {
        Image(
            painter = painterResource(Image.less),
            contentDescription = "less icon",
            colorFilter = ColorFilter.tint(Colors.boldText),
            modifier = Modifier.size(Dimension.mIconHeight)
        )
    }
}

@Composable
fun MiddleText(localCount: Int?, isLoading: Boolean, withIcon: Boolean = true) {
    fun counterText(countValue: Int?): String {
        if (countValue == null) return "-"
        return localCount.toString()
    }

    Row(
        modifier = Modifier
            .height(48.dp)
            .width(30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Colors.boldText, modifier = Modifier.size(16.dp))
        } else {
            Text(
                text = counterText(localCount),
                color = Colors.primary,
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
            if(withIcon) {
            Spacer(modifier = Modifier.size(4.dp))

            Image(
                painter = painterResource(R.drawable.icon_portion),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Colors.primary),
                modifier = Modifier.size(Dimension.lIconHeight)
            )
            }
        }
    }
}

@Composable
fun Plus(increase: () -> Unit, isDisable: Boolean) {
    IconButton(
        onClick = { increase() },
        enabled = !isDisable,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))

    ) {
        Image(
            painter = painterResource(Image.plus),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Colors.boldText),
            modifier = Modifier.size(Dimension.mIconHeight))
    }
}
