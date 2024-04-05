package ai.mealz.marmitonApp.ui.home

import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentHomeBinding
import ai.mealz.marmitonApp.ui.recipeDetail.RecipeDetailFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

      root.findViewById<Button>(R.id.button_test)
            .setOnClickListener {
                showDialog()
            }

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    fun showDialog() {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        val newFragment: DialogFragment = RecipeDetailFragment()
        newFragment.show(ft, "dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}