package ai.mealz.marmitonApp.ui.home

import ai.mealz.core.Mealz
import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.ui.recipeDetail.RecipeDetailFragment
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private val firstRecipeId: String = "22509"
    private val secondRecipeId: String = "14472"
    private lateinit var buttonShowPriceRecipe1: Button
    private lateinit var buttonShowPriceRecipe2: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonShowPriceRecipe1 = view.findViewById(R.id.button_show_price_recipe_1)
        buttonShowPriceRecipe2 = view.findViewById(R.id.button_show_price_recipe_2)

        // Recipe 1 Launch button
        val launchRecipe1Button: Button = view.findViewById(R.id.button_launch_recipe_1)
        launchRecipe1Button.setOnClickListener {
            // Handle the launch action for Recipe 1
            openRecipeDetailDialog(firstRecipeId)
        }

        // Observe the LiveData from ViewModel to update the button text
        homeViewModel.textRecipe1.observe(viewLifecycleOwner, Observer { newText ->
            buttonShowPriceRecipe1.text = newText
            // Disable the button if it's no longer "Show Price"
            if (newText != "Show Price") {
                buttonShowPriceRecipe1.isEnabled = false
            }
        })

        // Set the click listener to fetch the price when the button is clicked
        buttonShowPriceRecipe1.setOnClickListener {
            // Launch a coroutine to fetch the price
            lifecycleScope.launch {
                homeViewModel.getPriceRecipe1(firstRecipeId)
            }
        }

        // Recipe 2 Launch button
        val launchRecipe2Button: Button = view.findViewById(R.id.button_launch_recipe_2)
        launchRecipe2Button.setOnClickListener {
            // Handle the launch action for Recipe 2
            openRecipeDetailDialog(secondRecipeId)
        }

        // Observe the LiveData from ViewModel to update the button text
        homeViewModel.textRecipe2.observe(viewLifecycleOwner, Observer { newText ->
            buttonShowPriceRecipe2.text = newText
            // Disable the button if it's no longer "Show Price"
            if (newText != "Show Price") {
                buttonShowPriceRecipe2.isEnabled = false
            }
        })

        // Set the click listener to fetch the price when the button is clicked
        buttonShowPriceRecipe2.setOnClickListener {
            // Launch a coroutine to fetch the price
            lifecycleScope.launch {
                homeViewModel.getPriceRecipe2(secondRecipeId)
            }
        }

        // Delete Cache button
        val deleteCacheButton: Button = view.findViewById(R.id.button_delete_cache)
        deleteCacheButton.setOnClickListener {
            // Handle the delete cache action
            deleteCache()
        }

        // listen to point of sale events
        Mealz.notifications.pointOfSale.listen {
            fetchPricesIfPreviouslyLoaded()
        }
    }

    private fun fetchPricesIfPreviouslyLoaded() {
        if (buttonShowPriceRecipe1.text != "Show Price")
            lifecycleScope.launch {
                homeViewModel.getPriceRecipe1(firstRecipeId)
            }
        if (buttonShowPriceRecipe2.text != "Show Price")
            lifecycleScope.launch {
                homeViewModel.getPriceRecipe2(secondRecipeId)
            }
    }


    private fun openRecipeDetailDialog(recipeId: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        val newFragment = RecipeDetailFragment.newInstance(recipeId)
        newFragment.show(ft, "dialog")
    }

    private fun deleteCache() {
        context?.getSharedPreferences("MEALZ_CORE", 0)?.edit()?.clear()?.apply()
    }
}