package ai.mealz.marmitonApp.config

import ai.mealz.core.Mealz
import ai.mealz.core.handler.LogHandler
import ai.mealz.core.init.option
import ai.mealz.core.init.sdkRequirement
import ai.mealz.core.model.SupplierProduct
import ai.mealz.core.subscription.publisher.BasketPublisher
import ai.mealz.core.subscription.subscriber.BasketSubscriber
import ai.mealz.marmiton.config.MiamTemplateManager
import ai.mealz.marmitonApp.data.model.BasketEvent
import ai.mealz.marmitonApp.data.model.Product
import android.content.Context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


object MealzManager : CoroutineScope by CoroutineScope(Dispatchers.Main), BasketPublisher,
    BasketSubscriber {

    private var isInitialized = false
    private lateinit var applicationContext: Context

    private lateinit var userId: String
    private lateinit var storeId: String
    private var enableUserProfiling: Boolean = true

    private var retailerBasketSubject = MutableStateFlow<MutableList<Product>>(mutableListOf())

    private var _basketMealzRecipeCountFlow = MutableStateFlow(0)
    val basketMealzRecipeCountFlow = _basketMealzRecipeCountFlow.asStateFlow()

    private var _basketMealzFlow = MutableStateFlow<BasketEvent>(BasketEvent.Loading)
    val basketMealzFlow = _basketMealzFlow.asStateFlow()

    val supplierKey = "ewogICAgICAgICJwcm92aWRlcl9pZCI6ICJtYXJtaXRvbiIKCSJwbGF1c2libGVfZG9tYWluZSI6ICJtaWFtLm1hcm1pdG9uLmFwcCIsCgkibWlhbV9vcmlnaW4iOiAibWFybWl0b24iLAoJIm9yaWdpbiI6ICJtaWFtLm1hcm1pdG9uLmFwcCIsCgkibWlhbV9lbnZpcm9ubWVudCI6ICJVQVQiCn0="

    /** should not be changed during session */
    private var enableLike: Boolean = true

    fun initialize(
        appContext: Context,
        basket: MutableList<Product>,
        userId: String,
        storeId: String
    ) = apply {
        
        if (userId.isBlank()) throw Exception("userId Cannot be null or empty or blank")
        if (storeId.isBlank()) throw Exception("storeId Cannot be null or empty or blank")

        if (isInitialized) return@apply
        applicationContext = appContext.applicationContext
        startMealz()
        MiamTemplateManager()
        isInitialized = true
        //setUser(userId)
        setStore(storeId)
        setEnableLike(enableLike)
        setUserProfiling(enableUserProfiling)
        setBasket(basket)
        Mealz.environment.setAllowsSponsoredProducts(true)
        Mealz.notifications.availability.listen {
            if(it)  println("🟢 Miam SDK is on and ready to be used")
            else     println("🔴 Miam SDK is off and not ready to be used")
        }

        Mealz.notifications.recipesCount.listen {
            println("recipes count by flow : $it")
            launch {
                _basketMealzRecipeCountFlow.emit(it)
            }
        }
    }

    private fun startMealz() {
        Mealz.Core() {
            sdkRequirement {
                key = supplierKey
                context = applicationContext
            }
            option { isAnonymousModeEnabled =  true}
        }
    }

    /******************************************************************
     *
     * Mandatory
     *
     *****************************************************************/

    /**
     * Set the current store of the current user
     * @param id the current store id
     */
    fun setStore(id: String) = apply {
        storeId = id
        Mealz.user.setStoreWithMealzId(storeId)
    }

    /**
     * Set the current user
     * @param id the current user id
     */
    fun setUser(id: String) = apply {
        checkIfSdkInitialized({
            userId = id
            Mealz.user.updateUserId(userId)
        })
    }

    /******************************************************************
     *
     * Optional
     *
     *****************************************************************/

    /**
     * Enable if allow to learn from the customer's choices and suggest more and more specific
     * recipes and products
     * @param enable true or false, the default value is true
     */
    fun setUserProfiling(enable: Boolean) = apply {
        enableUserProfiling = enable
        Mealz.user.setProfilingAllowance(enableUserProfiling)
    }

    /**
     * Allow to display likes on recipes
     * @param enable true or false according if we want to show like on recipes. Should be set only
     * at Miam SDK initialization
     */
    private fun setEnableLike(enable: Boolean) {
        Mealz.user.setEnableLike(enable)
    }

    /**
     * Check if Miam SDK is well initialized before doing some actions
     * @param whenTrue the lambda to execute if the Miam SDK is well initialized
     * @param whenFalse the lambda to execute if the Miam SDK is not initialized yet
     */
    private fun checkIfSdkInitialized(
        whenTrue: () -> Unit,
        whenFalse: () -> Unit = { throw IllegalAccessException("We must init the Miam SDK well") }
    ) {
        if (!isInitialized) {
            whenFalse()
        } else {
            whenTrue()
        }
    }

    /******************************************************************
     *
     * Basket
     *
     *****************************************************************/

    fun setBasket(products: MutableList<Product>) {
        retailerBasketSubject = MutableStateFlow(products)
    }

    private fun productToSupplierProduct(product: Product): SupplierProduct  {
        return SupplierProduct(
            id = product.id,
            productIdentifier = product.identifier,
            quantity = product.quantity,
            name = product.attributes.name
        )
    }
    override fun receive(event: List<SupplierProduct>) {
        pushProductToRetailer(event)
    }

    private fun pushProductToRetailer(retailerProducts: List<SupplierProduct>) {
        retailerProducts.forEach { rp ->
            val productToUpdateIdx = retailerBasketSubject.value.indexOfFirst { it.id == rp.id }
            if (productToUpdateIdx == -1) {
                runBlocking {
                    withContext(Dispatchers.Default) {
                        Product.empty
                    }.let {
                        retailerBasketSubject.value.add(it.copy(quantity = rp.quantity))
                    }
                }
            } else if (rp.quantity == 0) {
                retailerBasketSubject.value.removeAt(productToUpdateIdx)
            } else {
                retailerBasketSubject.value[productToUpdateIdx] =
                    retailerBasketSubject.value[productToUpdateIdx].copy(quantity = rp.quantity)
            }
        }

        launch {
            retailerBasketSubject.emit(retailerBasketSubject.value)
            _basketMealzFlow.emit(BasketEvent.Changed(retailerBasketSubject.value))
        }
    }

    override var initialValue: List<SupplierProduct> = retailerBasketSubject.value.map { productToSupplierProduct(it) }


    override fun onBasketUpdate(sendUpdateToSDK: (List<SupplierProduct>) -> Unit) {
        launch {
            retailerBasketSubject.collect { state ->
                LogHandler.debug("[New Mealz] push basket update from supplier")
                sendUpdateToSDK(state.map { SupplierProduct(it.id, it.quantity) })
            }
        }
    }
}
