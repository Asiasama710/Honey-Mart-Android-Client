package org.the_chance.honeymart.ui.feature.cart

import org.the_chance.honeymart.domain.model.CartEntity
import org.the_chance.honeymart.domain.model.CartProductsEntity
import org.the_chance.honeymart.domain.util.ErrorHandler

data class CartUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val error: ErrorHandler? = null,
    val total: Double? = 0.0,
    val products: List<CartListProductUiState> = emptyList(),
    val bottomSheetIsDisplayed: Boolean = false
)

data class CartListProductUiState(
    val productId: Long = 0L,
    val productName: String = "",
    val productPrice: Double = 0.0,
    val productCount: Int= 0,
    val productImage: List<String> = emptyList()
)

fun CartEntity.toCartListProductUiState(): CartUiState {
    return CartUiState(
        total = total,
        products = products.toCartProductUiState()
    )
}

fun List<CartProductsEntity>.toCartProductUiState(): List<CartListProductUiState> {
    return this.map {
        CartListProductUiState(
            productId = it.id,
            productName = it.name,
            productPrice = it.price,
            productCount = it.count,
            productImage = it.images
        )
    }
}

fun CartUiState.firstTimeLoading(state: CartUiState): Boolean {
    return state.isLoading && state.products.isNotEmpty()
}

fun CartUiState.isError(state: CartUiState): Boolean {
    return state.isError
}



