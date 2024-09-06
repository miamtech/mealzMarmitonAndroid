package ai.mealz.marmitonApp

import ai.mealz.core.Mealz
import ai.mealz.marmitonApp.config.MealzManager
import ai.mealz.marmitonApp.databinding.ActivityMainBinding
import ai.mealz.marmitonApp.ui.storeLocator.StoreLocatorFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_my_basket
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        MealzManager.initialize(this)
        Mealz.user.setStoreLocatorRedirection {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val newFragment = StoreLocatorFragment()
            newFragment.show(ft, "dialog")
        }
    }
}