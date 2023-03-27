package com.andrescoulson.userceibalist.domain.model

data class Post(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)