package org.the_chance.honeymart.ui.add_product

import org.the_chance.honeymart.domain.model.ProductEntity
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.domain.util.ValidationState

data class AddProductUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error: ErrorHandler? = null,
    val id: Long = 0L,
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val images: List<ByteArray> = emptyList(),
    val productNameState: ValidationState = ValidationState.VALID_TEXT_FIELD,
    val productPriceState: ValidationState = ValidationState.VALID_TEXT_FIELD,
    val productDescriptionState: ValidationState = ValidationState.VALID_TEXT_FIELD,
)

fun AddProductUiState.showButton(): Boolean {
    return name.isNotBlank()
            && price.isNotBlank()
            && description.isNotBlank()
            && !isLoading
            && productNameState == ValidationState.VALID_TEXT_FIELD
            && productPriceState == ValidationState.VALID_TEXT_FIELD
            && productDescriptionState == ValidationState.VALID_TEXT_FIELD
}