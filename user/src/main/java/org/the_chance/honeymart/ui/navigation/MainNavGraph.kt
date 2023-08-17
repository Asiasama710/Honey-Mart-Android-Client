package org.the_chance.honeymart.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import org.the_chance.honeymart.ui.LocalNavigationProvider

@Composable
fun MainNavGraph() {
    val navController = LocalNavigationProvider.current

    NavHost(
        navController = navController,
        startDestination = Graph.HOME
    ) {
        authNavGraph()
        homeNavGraph()
        cartNavGraph()
        orderNavGraph()
        wishListNavGraph()
    }
}

object Graph {
    const val AUTH_GRAPH = "auth_graph"
    const val HOME = "home_graph"
    const val CART = "cart_graph"
    const val ORDERS = "search_screen"
    const val WISH_LIST = "wish_list_graph"
}