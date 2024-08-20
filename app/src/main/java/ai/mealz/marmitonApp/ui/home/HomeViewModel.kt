package ai.mealz.marmitonApp.ui.home

import ai.mealz.core.Mealz
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Show Price"
    }

    suspend fun getPrice(v: View) {
       if(_text.value == "Show Price" ){
           _text.value =  Mealz.recipe.getPriceOrRedirect("22509", 4).await()?.toString() ?: return
       }
    }

    val text: LiveData<String> = _text
}