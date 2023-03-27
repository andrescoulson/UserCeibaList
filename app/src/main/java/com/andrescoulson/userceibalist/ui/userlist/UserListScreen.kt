package com.andrescoulson.userceibalist.ui.userlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.andrescoulson.userceibalist.R
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.ui.theme.Green200
import com.andrescoulson.userceibalist.ui.userlist.UiState.*
import java.util.*


@Composable
fun UserListScreen(userListViewModel: UserListViewModel, navController: NavHostController) {

    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<UiState<List<User>>>(
        initialValue = Loading,
        key1 = lifeCycle,
        key2 = userListViewModel
    ) {

        lifeCycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            userListViewModel.userListUiState.collect {
                value = it
            }
        }
    }

    when (uiState) {
        is Error -> {}
        Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        is Success -> {
            val userList = (uiState as Success).data
            ListScreen(
                list = userList,
                userListViewModel = userListViewModel,
                modifier = Modifier.fillMaxSize(),
                navController
            )
        }
    }
}

@Composable
fun Toolbar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = 4.dp
    )
}


@Composable
fun ListScreen(list: List<User>, userListViewModel: UserListViewModel, modifier: Modifier, navController: NavHostController) {
    Column(modifier = modifier) {
        Toolbar(title = stringResource(R.string.toolbar_title))
        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
        SearchView(userListViewModel = userListViewModel)
        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
        UserList(list, userListViewModel, navController)
    }
}

@Composable
fun UserList(users: List<User>, userListViewModel: UserListViewModel, navController: NavHostController) {
    val searchText by userListViewModel.searchText.collectAsState()
    LazyColumn {
        items(users.filter {
            it.name.contains(searchText, ignoreCase = true)
        }) { user ->
            CardItemUser(user = user, userListViewModel, navController)
        }
    }
}

@Composable
fun CardItemUser(user: User, userListViewModel: UserListViewModel, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 5.dp), elevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.h6,
                color = Green200
            )
            TextWithIcon(
                text = user.phone,
                modifier = Modifier,
                style = MaterialTheme.typography.body1,
                icon = Icons.Default.Phone
            )
            TextWithIcon(
                text = user.email,
                modifier = Modifier,
                style = MaterialTheme.typography.body1,
                icon = Icons.Default.Email
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        userListViewModel.selectUser(user)
                        navController.navigate("UserDetail")
                    },
                text = stringResource(R.string.see_posts).toUpperCase(Locale.US),
                style = MaterialTheme.typography.subtitle1,
                color = Green200
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun TextWithIcon(text: String, modifier: Modifier, style: TextStyle, icon: ImageVector) {
    Row(modifier = modifier.wrapContentWidth()) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Green200
        )
        Text(text = text, style = style, modifier = modifier, color = Green200)
    }
}

@Composable
fun SearchView(
    userListViewModel: UserListViewModel
) {
    val searchText by userListViewModel.searchText.collectAsState()
    TextField(
        value = searchText,
        onValueChange = userListViewModel::onSearchTextChange,
        label = { Text(text = stringResource(id = R.string.find_user)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 0.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
}