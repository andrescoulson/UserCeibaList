package com.andrescoulson.userceibalist.data.resources.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrescoulson.userceibalist.domain.model.User

@Entity
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("phone") val phone: String
)

fun UserEntity.toUserMap(): User = this.run {
    User(
        name = name,
        id = id,
        phone = phone,
        email = email
    )
}
