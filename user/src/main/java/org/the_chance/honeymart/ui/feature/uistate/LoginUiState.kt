package org.the_chance.honeymart.ui.feature.uistate

import org.the_chance.honeymart.domain.util.ValidationState

/**
 * Created by Aziza Helmy on 6/16/2023.
 */

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: Int = 0,
    val email: String = "",
    val password: String = "",
    val emailState: ValidationState = ValidationState.VALID_EMAIL,
    val passwordState: ValidationState = ValidationState.VALID_PASSWORD,
    val validationState: ValidationState = ValidationState.SUCCESS,
)