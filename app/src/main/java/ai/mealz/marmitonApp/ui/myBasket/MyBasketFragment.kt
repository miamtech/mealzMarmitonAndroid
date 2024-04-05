package ai.mealz.marmitonApp.ui.myBasket

import ai.mealz.marmitonApp.R
import ai.mealz.marmitonApp.databinding.FragmentMyBasketBinding
import ai.mealz.sdk.components.myBasket.MyBasket
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class MyBasketFragment : Fragment() {

    private var _binding: FragmentMyBasketBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyBasketBinding.inflate(inflater, container, false)
        val root: View = binding.root
        root.findViewById<MyBasket>(R.id.my_basket).bind {  }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}