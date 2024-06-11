package ai.mealz.marmitonApp.ui.recipeDetail

import ai.mealz.core.Mealz
import ai.mealz.marmiton.config.components.mealzJourney.MealzJourney
import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentRecipeDetailBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController


class RecipeDetailFragment : DialogFragment() {

    private var _binding: FragmentRecipeDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Mealz.user.setSignInRedirection {
            dismiss()
        }
        Mealz.user.setStoreLocatorRedirection {
            dismiss()
            findNavController().navigate(R.id.navigation_store_locator)

        }
        root.findViewById<MealzJourney>(R.id.recipe_detail)
            .bind("22509") {
                dismiss()
                findNavController().navigate(R.id.navigation_my_basket)
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}