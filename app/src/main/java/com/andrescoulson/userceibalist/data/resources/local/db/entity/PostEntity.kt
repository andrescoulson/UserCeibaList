package com.andrescoulson.userceibalist.data.resources.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrescoulson.userceibalist.domain.model.Post

@Entity
data class PostEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("body") val body: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("userId") val userId: Int
)

fun PostEntity.toPostMap() = this.run {
    Post(
        body = body,
        title = title,
        userId = userId,
        id = id
    )
}