package ai.mealz.marmitonApp.ui.home

import ai.mealz.marmitonApp.R
import ai.mealz.sdk.components.recipeDetailButton.RecipeDetailButton
import ai.mealz.sdk.components.recipePrice.RecipePrice
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val firstRecipeId: String = "22509"
    private val secondRecipeId: String = "14472"
    private lateinit var buttonShowPriceRecipe1: RecipePrice
    private lateinit var buttonShowPriceRecipe2: RecipePrice

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonShowPriceRecipe1 = view.findViewById(R.id.recipe1_price)
        buttonShowPriceRecipe1.bind(firstRecipeId, 4, true)
        buttonShowPriceRecipe2 = view.findViewById(R.id.recipe2_price)
        buttonShowPriceRecipe2.bind(secondRecipeId, 4, true)

        view.findViewById<RecipeDetailButton>(R.id.recipe1_button).apply {
            bind(firstRecipeId, isExtId = true)
        }
        view.findViewById<RecipeDetailButton>(R.id.recipe2_button).apply {
            bind(secondRecipeId)
        }

        // Delete Cache button
        val deleteCacheButton: Button = view.findViewById(R.id.button_delete_cache)
        deleteCacheButton.setOnClickListener {
            // Handle the delete cache action
            deleteCache()
        }
    }

    private fun deleteCache() {
        context?.getSharedPreferences("MEALZ_CORE", 0)?.edit()?.clear()?.apply()
    }
}