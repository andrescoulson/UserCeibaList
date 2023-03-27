package com.andrescoulson.userceibalist.data.repository

import com.andrescoulson.userceibalist.data.entity.toMap
import com.andrescoulson.userceibalist.data.entity.toPostMap
import com.andrescoulson.userceibalist.data.resources.local.LocalDataSource
import com.andrescoulson.userceibalist.data.resources.remote.RemoteDataSource
import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    override fun getUserList(): Flow<List<User>> {
        return localDataSource.users.flatMapLatest { users ->
            if (users.isNotEmpty()) {
                flowOf(users)
            } else {
                fetchUsers()
            }
        }
    }

    private fun fetchUsers(): Flow<List<User>> = flow {
        val userList = remoteDataSource.getUserList()
        localDataSource.addUsers(userList)
        emit(userList.map { it.toMap() })
    }

    override fun getPostsById(userId: Int): Flow<List<Post>> {
        return localDataSource.getPostsById(userId).flatMapLatest { posts ->
            if (posts.isNotEmpty()) {
                flowOf(posts)
            } else {
                fetchPostById(userId)
            }
        }
    }

    private fun fetchPostById(userId: Int): Flow<List<Post>> = flow {
        val postList = remoteDataSource.getPostsById(userId)
        localDataSource.addPosts(postList)
        emit(postList.map { it.toPostMap() })
    }

    override fun getPosts(): Flow<List<Post>> {
        return localDataSource.posts.flatMapLatest { posts ->
            if (posts.isNotEmpty()) {
                flowOf(posts)
            } else {
                fetchPosts()
            }
        }
    }

    private fun fetchPosts(): Flow<List<Post>> = flow {
        val posts = remoteDataSource.getPosts()
        localDataSource.addPosts(posts)
        emit(posts.map { it.toPostMap() })
    }
}