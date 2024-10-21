package ai.mealz.marmitonApp.config

import ai.mealz.core.Mealz
import ai.mealz.core.init.option
import ai.mealz.core.init.sdkRequirement
import ai.mealz.marmiton.config.MiamTemplateManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


object MealzManager : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var isInitialized = false
    private lateinit var applicationContext: Context

    private var enableUserProfiling: Boolean = true
    private var _basketMealzRecipeCountFlow = MutableStateFlow(0)

    val supplierKey =
        "ewogICAgICAgICJwcm92aWRlcl9pZCI6ICJtYXJtaXRvbiIKCSJwbGF1c2libGVfZG9tYWluZSI6ICJtaWFtLm1hcm1pdG9uLmFwcCIsCgkibWlhbV9vcmlnaW4iOiAibWFybWl0b24iLAoJIm9yaWdpbiI6ICJtaWFtLm1hcm1pdG9uLmFwcCIsCgkibWlhbV9lbnZpcm9ubWVudCI6ICJQUk9EIgp9"
    val supplierKeyUAT =
        "ewogICAgICAgICJwcm92aWRlcl9pZCI6ICJtYXJtaXRvbiIKCSJwbGF1c2libGVfZG9tYWluZSI6ICJtaWFtLm1hcm1pdG9uLmFwcCIsCgkibWlhbV9vcmlnaW4iOiAibWFybWl0b24iLAoJIm9yaWdpbiI6ICJtaWFtLm1hcm1pdG9uLmFwcCIsCgkibWlhbV9lbnZpcm9ubWVudCI6ICJVQVQiCn0="

    /** should not be changed during session */
    private var enableLike: Boolean = true

    fun initialize(appContext: Context) = apply {

        if (isInitialized) return@apply
        applicationContext = appContext.applicationContext
        startMealz()
        MiamTemplateManager()
        isInitialized = true
        setEnableLike(enableLike)
        setUserProfiling(enableUserProfiling)
        Mealz.environment.setAllowsSponsoredProducts(true)
        Mealz.notifications.availability.listen {
            if (it) println("🟢 Miam SDK is on and ready to be used")
            else println("🔴 Miam SDK is off and not ready to be used")
        }

        Mealz.notifications.recipesCount.listen {
            println("recipes count by flow : $it")
            launch {
                _basketMealzRecipeCountFlow.emit(it)
            }
        }
    }

    private fun startMealz() {
        Mealz.Core {
            sdkRequirement {
                key = supplierKey
                context = applicationContext
            }
            option { isAnonymousModeEnabled = true }
        }
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
}
