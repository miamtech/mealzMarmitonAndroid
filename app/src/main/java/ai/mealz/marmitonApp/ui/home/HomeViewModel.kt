package ai.mealz.marmitonApp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {

    // LiveData for Recipe 1 button text
    private val _textRecipe1 = MutableLiveData<String>().apply {
        value = "Show Price"
    }

    // LiveData for Recipe 1 button text
    private val _textRecipe2 = MutableLiveData<String>().apply {
        value = "Show Price"
    }
}