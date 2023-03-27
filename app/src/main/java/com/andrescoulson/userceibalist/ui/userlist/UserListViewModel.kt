package com.andrescoulson.userceibalist.ui.userlist

import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.domain.usecase.GetPostListByIdUseCase
import com.andrescoulson.userceibalist.domain.usecase.GetUserListUseCase
import com.andrescoulson.userceibalist.ui.userlist.UiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    getUserListUseCase: GetUserListUseCase,
    private val getPostListByIdUseCase: GetPostListByIdUseCase
) : ViewModel() {

    val userListUiState: StateFlow<UiState<List<User>>> = getUserListUseCase().map(::Success)
        .catch { UiState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String>
        get() = _searchText.asStateFlow()

    private val _userSelected: MutableLiveData<User?> = MutableLiveData(null)
    val userSelected: LiveData<User?>
        get() = _userSelected


    val postListUiState: MutableStateFlow<UiState<List<Post>>> = MutableStateFlow(UiState.Loading)

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    fun getPostById() {
        viewModelScope.launch {
            _userSelected.value?.let {
                getPostListByIdUseCase(it)
                    .catch { e -> postListUiState.value = UiState.Error(e) }
                    .collect { posts ->
                        postListUiState.value = UiState.Success(posts)
                    }
            }

        }
    }

    fun selectUser(user: User) {
        _userSelected.value = user
    }

    init {
        viewModelScope.launch {
            getUserListUseCase()
        }
    }
}