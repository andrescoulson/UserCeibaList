package com.andrescoulson.userceibalist.ui.userlist


sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Error(val throwable: Throwable) : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}