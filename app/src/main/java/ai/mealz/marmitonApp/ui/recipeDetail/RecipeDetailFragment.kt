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
    private val binding get() = _binding!!

    private var recipeId: String? = null

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipeId = it.getString(ARG_RECIPE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Use the passed recipeId instead of the hardcoded value
        recipeId?.let { id ->
            root.findViewById<MealzJourney>(R.id.recipe_detail).bind(id) {
                dismiss()
                findNavController().navigate(R.id.navigation_my_basket)
            }
        } ?: run {
            // Handle the case where recipeId is not provided
            dismiss()
        }

        Mealz.user.setSignInRedirection {
            dismiss()
        }
        Mealz.user.setStoreLocatorRedirection {
            dismiss()
            findNavController().navigate(R.id.navigation_store_locator)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_RECIPE_ID = "recipe_id"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param recipeId The ID of the recipe to display.
         * @return A new instance of fragment RecipeDetailFragment.
         */
        fun newInstance(recipeId: String): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putString(ARG_RECIPE_ID, recipeId)
            fragment.arguments = args
            return fragment
        }
    }
}
