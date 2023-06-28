package org.the_chance.honeymart.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.the_chance.user.R
import org.the_chance.user.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_host) as NavHostFragment
        val navController = navHostFragment.navController

        setupNavigation(navController)

    }

    private fun setupNavigation(navController: NavController) {
        val navView = binding.bottomNavigationView
        val cart = setOf(
            R.id.cartFragment,
            R.id.cartBottomFragment
        )
        val wishList = setOf(
            R.id.wishListFragment
        )
        val orders = setOf(
            R.id.ordersFragment,
            R.id.orderDetailsFragment
        )

        setOnBottomNavigationListener(navView, navController, cart, orders, wishList)

        navView.setupWithNavController(navController)
    }

    private fun setOnBottomNavigationListener(
        navView: BottomNavigationView,
        navController: NavController,
        cart: Set<Int>,
        orders: Set<Int>,
        wishList: Set<Int>,
    ) {
        navView.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.markets_graph -> {
                    navController.popBackStack(R.id.marketsFragment, false)
                }

                R.id.cart_graph -> {
                    if (navController.currentDestination?.id in cart) {
                        navController.popBackStack(R.id.cartFragment, false)
                    } else {
                        navController.popBackStack(R.id.markets_graph, false)
                    }
                }

                R.id.orders_graph -> {
                    if (navController.currentDestination?.id in orders) {
                        navController.popBackStack(R.id.ordersFragment, false)
                    } else {
                        navController.popBackStack(R.id.markets_graph, false)
                    }
                }

                R.id.wish_list_graph -> {
                    if (navController.currentDestination?.id in wishList) {
                        navController.popBackStack(R.id.wishListFragment, false)
                    } else {
                        navController.popBackStack(R.id.markets_graph, false)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_host)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

