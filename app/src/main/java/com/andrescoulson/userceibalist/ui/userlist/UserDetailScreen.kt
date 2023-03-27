package com.andrescoulson.userceibalist.ui.userlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.andrescoulson.userceibalist.R
import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.ui.theme.Green200

@Composable
fun UserDetailScreen(userListViewModel: UserListViewModel = hiltViewModel()) {
    val user by userListViewModel.userSelected.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar(title = stringResource(R.string.toolbar_title))
        Spacer(modifier = Modifier.padding(0.dp, 5.dp))
        UserDetails(user, Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        PostList(userListViewModel)
    }
}

@Composable
fun UserDetails(user: User?, modifier: Modifier) {
    Text(
        text = user?.name.orEmpty(),
        style = MaterialTheme.typography.h4,
        color = Green200,
        modifier = modifier
    )
    TextWithIcon(
        text = user?.phone.orEmpty(),
        style = MaterialTheme.typography.h6,
        icon = Icons.Default.Phone,
        modifier = modifier
    )
    TextWithIcon(
        text = user?.email.orEmpty(),
        style = MaterialTheme.typography.h6,
        icon = Icons.Default.Email,
        modifier = modifier
    )

}

@Composable
fun PostList(userListViewModel: UserListViewModel) {
    LaunchedEffect(key1 = true) {
        userListViewModel.getPostById()
    }

    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<UiState<List<Post>>>(
        initialValue = UiState.Loading,
        key1 = lifeCycle,
        key2 = userListViewModel
    ) {

        lifeCycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            userListViewModel.postListUiState.collect {
                value = it
            }
        }
    }

    when (uiState) {
        is UiState.Error -> ErrorScreen()
        UiState.Loading -> LoadingBox()
        is UiState.Success -> {
            val posts = (uiState as UiState.Success).data
            PostScreen(posts)
        }
    }
}

@Composable
fun ErrorScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.error_post_message),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun PostScreen(posts: List<Post>) {
    Text(
        text = stringResource(R.string.posts),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        style = MaterialTheme.typography.subtitle1
    )
    LazyColumn {
        items(posts) { post ->
            CardPosts(post)
        }
    }
}

@Composable
fun CardPosts(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 5.dp), elevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.h6,
                color = Green200
            )
            Spacer(modifier = Modifier.padding(vertical = 5.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.body1,
                color = Green200
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }

    }
}

@Composable
fun LoadingBox() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}
