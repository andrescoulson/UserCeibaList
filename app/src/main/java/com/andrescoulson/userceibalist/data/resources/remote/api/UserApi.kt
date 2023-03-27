package com.andrescoulson.userceibalist.data.resources.remote.api

import com.andrescoulson.userceibalist.data.entity.PostEntity
import com.andrescoulson.userceibalist.data.entity.UserEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("users/")
    suspend fun getUserList(): List<UserEntity>

    @GET("posts/")
    suspend fun getPostList(): List<PostEntity>

    @GET("posts/")
    suspend fun getPostListById(@Query("userId") userId: Int): List<PostEntity>

}