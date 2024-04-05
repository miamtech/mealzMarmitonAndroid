package ai.mealz.marmitonApp.data.model

    sealed class BasketEvent {
        object Loading : BasketEvent()
        class Changed(val products: List<Product>) : BasketEvent()
    }
