package org.the_chance.honeymart.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.the_chance.honeymart.domain.util.InvalidEmailException
import org.the_chance.honeymart.domain.util.InvalidEmailOrPasswordException
import org.the_chance.honeymart.domain.util.InvalidRegisterException
import org.the_chance.honeymart.domain.util.NoNetworkException
import org.the_chance.honeymart.domain.util.UnAuthorizedException
import org.the_chance.honeymart.util.EventHandler
import java.io.IOException

abstract class BaseViewModel<T, E>(initialState: T) : ViewModel() {

    abstract val TAG: String
    protected open fun log(message: String) {
        Log.e(TAG, message)
    }

    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    protected val _effect = MutableSharedFlow<EventHandler<E>>()
    val effect = _effect.asSharedFlow()


    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (t: Exception) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                val result = function()
                Log.e(TAG, "tryToExecute:$result ")
                onSuccess(result)
            } catch (exception: InvalidRegisterException) {
                log("tryToExecute error InvalidRegisterException: ${exception.message}")
//                onError(ErrorHandler.InvalidRegister)
            } catch (exception: InvalidEmailException) {
                log("tryToExecute error InvalidEmailException: ${exception.message}")
//                onError(ErrorHandler.EmailIsExist)
            } catch (exception: NoNetworkException) {
                log("tryToExecute error InvalidEmailException: ${exception.message}")
//                onError(ErrorHandler.NoNetwork)
            } catch (exception: UnAuthorizedException) {
                log("tryToExecute error Exception: ${exception.message}")
            } catch (exception: InvalidEmailOrPasswordException) {
//                onError(ErrorHandler.InvalidEmailAndPassword)
                log("tryToExecute error InvalidEmailOrPasswordException: ${exception.message}")
            } catch (exception: IOException) {
//                onError(ErrorHandler.NoNetwork)
                log("tryToExecute error Exception: ${exception.message}, ${exception.cause}")
            }
        }
    }
}

