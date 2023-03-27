package com.andrescoulson.userceibalist.data.entity

import com.andrescoulson.userceibalist.domain.model.Post
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostEntity(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)

fun PostEntity.toPostMap() = this.run {
    Post(
        title = title,
        id = id,
        body = body,
        userId = userId
    )
}

fun PostEntity.toMapDbEntity() = this.run {
    com.andrescoulson.userceibalist.data.resources.local.db.entity.PostEntity(
        id = id,
        userId = userId,
        body = body,
        title = title
    )
}