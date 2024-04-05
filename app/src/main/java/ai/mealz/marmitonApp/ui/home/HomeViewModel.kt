package ai.mealz.marmitonApp.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    fun goToBuy(v: View) {
        when (v.id) {
        }
    }

    val text: LiveData<String> = _text
}