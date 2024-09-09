package ai.mealz.marmitonApp.ui.home

import ai.mealz.core.Mealz
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HomeViewModel : ViewModel() {

    // LiveData for Recipe 1 button text
    private val _textRecipe1 = MutableLiveData<String>().apply {
        value = "Show Price"
    }

    val textRecipe1: LiveData<String> = _textRecipe1

    // Function to get the price for Recipe 1
    suspend fun getPriceRecipe1(recipeId: String) {
        // Assuming Mealz.recipe.getPriceOrRedirect returns a Deferred or similar suspendable result
        val price = withContext(Dispatchers.IO) {
            Mealz.recipe.getPriceOrRedirect(recipeId, 4).await()
        }
        price?.let {
            _textRecipe1.value = it.toString()
        }
    }

    // LiveData for Recipe 1 button text
    private val _textRecipe2 = MutableLiveData<String>().apply {
        value = "Show Price"
    }

    val textRecipe2: LiveData<String> = _textRecipe2

    // Function to get the price for Recipe 1
    suspend fun getPriceRecipe2(recipeId: String) {
        // Assuming Mealz.recipe.getPriceOrRedirect returns a Deferred or similar suspendable result
        val price = withContext(Dispatchers.IO) {
            Mealz.recipe.getPriceOrRedirect(recipeId, 4).await()
        }
        price?.let {
            _textRecipe2.value = it.toString()
        }
    }
}