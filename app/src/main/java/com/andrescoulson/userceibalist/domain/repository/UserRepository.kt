package com.andrescoulson.userceibalist.domain.repository

import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserList(): Flow<List<User>>
    fun getPostsById(userId: Int): Flow<List<Post>>
    fun getPosts(): Flow<List<Post>>
}