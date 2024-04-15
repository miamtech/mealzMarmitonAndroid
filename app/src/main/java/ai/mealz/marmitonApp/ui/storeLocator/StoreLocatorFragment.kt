package ai.mealz.marmitonApp.ui.storeLocator

import ai.mealz.core.Mealz
import ai.mealz.marmitonApp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ai.mealz.marmitonApp.databinding.FragmentStoreLocatorBinding
import android.widget.Button

class StoreLocatorFragment : Fragment() {

    private var _binding: FragmentStoreLocatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStoreLocatorBinding.inflate(inflater, container, false)
        val root: View = binding.root
        root.findViewById<Button>(R.id.button_store_A)
            .setOnClickListener {
                Mealz.user.setStoreId("25910")
            }
        root.findViewById<Button>(R.id.button_store_B)
            .setOnClickListener {
                Mealz.user.setStoreId("25911")
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}