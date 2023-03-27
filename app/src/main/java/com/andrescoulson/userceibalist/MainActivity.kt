package com.andrescoulson.userceibalist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andrescoulson.userceibalist.ui.theme.UserCeibaListTheme
import com.andrescoulson.userceibalist.ui.userlist.UserDetailScreen
import com.andrescoulson.userceibalist.ui.userlist.UserListScreen
import com.andrescoulson.userceibalist.ui.userlist.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userListViewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserCeibaListTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "UserScreen"
                ) {
                    composable("UserScreen") {
                        UserListScreen(userListViewModel, navController)
                    }
                    composable("UserDetail") {
                        UserDetailScreen(userListViewModel)
                    }
                }

            }
        }
    }
}
