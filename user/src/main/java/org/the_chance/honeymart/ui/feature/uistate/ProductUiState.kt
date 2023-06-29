package org.the_chance.honeymart.ui.feature.uistate

import org.the_chance.honeymart.domain.model.ProductEntity
import org.the_chance.honeymart.domain.util.ErrorHandler

data class ProductsUiState(
    val isLoadingCategory: Boolean = false,
    val isLoadingProduct: Boolean = false,
    val error: ErrorHandler? = null,
    val isError: Boolean = false,
    val position: Int = 0,
    val products: List<ProductUiState> = emptyList(),
    val categories: List<CategoryUiState> = emptyList(),
    val categoryId: Long = 0L
)

data class ProductUiState(
    val productId: Long? = 0L,
    val productName: String? = "",
    val productDescription: String? = "",
    val productPrice: Double? = 0.0,
    val isFavorite: Boolean? = false,
    val productImages:List<String>? = emptyList()
)

fun ProductEntity.toProductUiState(): ProductUiState {
    return ProductUiState(
        productId = productId,
        productName = productName,
        productDescription = productDescription,
        productPrice = ProductPrice,
        productImages = productImages
    )
}