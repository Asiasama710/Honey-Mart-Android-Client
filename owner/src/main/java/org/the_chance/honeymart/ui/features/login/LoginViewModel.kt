package org.the_chance.honeymart.ui.features.login

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.the_chance.honeymart.domain.usecase.CheckAdminApproveUseCase
import org.the_chance.honeymart.domain.usecase.LoginOwnerUseCase
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.base.BaseViewModel
import org.the_chance.honeymart.ui.features.signup.FieldState
import org.the_chance.honeymart.ui.features.signup.ValidationToast
import org.the_chance.honeymart.ui.util.StringDictionary
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginOwnerUseCase: LoginOwnerUseCase,
    private val stringResourceImpl: StringDictionary,
    private val checkAdminApprove: CheckAdminApproveUseCase
) : BaseViewModel<LoginUiState, LoginUiEffect>(LoginUiState()),
    LoginInteractionListener {

    override val TAG: String = this::class.java.simpleName

    init {
        listenToCheckAdminApprove()
    }

    private fun listenToCheckAdminApprove() {
        _state.update { it.copy(authLoading = true) }
        tryToExecute(
            { checkAdminApprove() },
            ::onCheckApproveSuccess,
            ::onCheckApproveError
        )
    }

    private fun onCheckApproveSuccess(isApproved: Boolean) {
        log(isApproved.toString())
        if (isApproved) {
            effectActionExecutor(_effect, LoginUiEffect.NavigateToCategoriesEffect)
        } else {
            effectActionExecutor(_effect, LoginUiEffect.NavigateToWaitingApproveEffect)
        }
    }

    private fun onCheckApproveError(error: ErrorHandler) {
        _state.update { it.copy(authLoading = false, error = error) }
        effectActionExecutor(_effect, LoginUiEffect.NavigateToLoginEffect)
    }

    // endregion

    // region Login
    override fun onClickLogin() {
        val validState = state.value.emailState.value.isNotBlank() &&
                state.value.passwordState.value.isNotBlank()
        if (validState) {
            loginOwner(
                email = state.value.emailState.value,
                password = state.value.passwordState.value
            )
        } else {
            _state.update {
                it.copy(
                    validationToast = ValidationToast(
                        isShow = true, message = stringResourceImpl.requiredFieldsMessageString
                    )
                )
            }
            effectActionExecutor(_effect, LoginUiEffect.ShowLoginErrorToastEffect)
        }
    }

    override fun onClickSignup() {
        effectActionExecutor(_effect, LoginUiEffect.ClickSignUpEffect)
    }

    private fun loginOwner(email: String, password: String) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            { loginOwnerUseCase(email, password) },
            ::onLoginSuccess,
            ::onLoginError,
        )
    }

    private fun onLoginSuccess(marketId: Long) {
        Log.d("Tarek", "$marketId")
        if (marketId == 0L) {
            effectActionExecutor(_effect, LoginUiEffect.NavigateToCreateMarketEffect)
        } else {
            _state.update { it.copy(isLoading = false, isError = false, error = null) }
            listenToCheckAdminApprove()
        }
    }

    private fun onLoginError(error: ErrorHandler) {
        val errorMessage = stringResourceImpl.errorString.getOrDefault(error, "")
        if (error is ErrorHandler.UnAuthorizedUser) {
            _state.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    error = error,
                    emailState = state.value.emailState.copy(errorState = errorMessage),
                    passwordState = state.value.passwordState.copy(errorState = errorMessage),
                    validationToast = ValidationToast(isShow = true, message = errorMessage)
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLoading = false,
                    isError = true,
                    error = error,
                    validationToast = ValidationToast(
                        isShow = true,
                        message = errorMessage
                    )
                )
            }
        }
        effectActionExecutor(_effect, LoginUiEffect.ShowLoginErrorToastEffect)
    }

    override fun onEmailInputChange(email: CharSequence) {
        _state.update {
            it.copy(emailState = FieldState(value = email.trim().toString(), errorState = ""))
        }
    }

    override fun onPasswordInputChanged(password: CharSequence) {
        _state.update {
            it.copy(passwordState = FieldState(value = password.trim().toString(), errorState = ""))
        }
    }

// endregion
}