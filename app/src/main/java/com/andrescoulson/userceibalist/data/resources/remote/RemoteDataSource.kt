package com.andrescoulson.userceibalist.data.resources.remote

import com.andrescoulson.userceibalist.data.entity.PostEntity
import com.andrescoulson.userceibalist.data.entity.UserEntity
import com.andrescoulson.userceibalist.data.resources.remote.api.UserApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {

    suspend fun getUserList(): List<UserEntity> {
        return userApi.getUserList()
    }

    suspend fun getPostsById(userId: Int): List<PostEntity> {
        return userApi.getPostListById(userId = userId)
    }

    suspend fun getPosts(): List<PostEntity> {
        return userApi.getPostList()
    }
}