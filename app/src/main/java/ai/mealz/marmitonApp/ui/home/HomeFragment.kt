package ai.mealz.marmitonApp.ui.home

import ai.mealz.core.Mealz
import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentHomeBinding
import ai.mealz.marmitonApp.ui.recipeDetail.RecipeDetailFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID


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
        textView.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{
                homeViewModel.getPrice(it)
            }
        }
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val hasStoreSwitch: Switch = root.findViewById<Switch>(R.id.change_store_switch)
        hasStoreSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Mealz.user.setStoreId("25910")
            else Mealz.user.setStoreId("")
        }

        val hasUserSwitch: Switch = root.findViewById<Switch>(R.id.change_user_switch)
        hasUserSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) Mealz.user.updateUserId("test_${UUID.randomUUID()}")
            else Mealz.user.updateUserId("")
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