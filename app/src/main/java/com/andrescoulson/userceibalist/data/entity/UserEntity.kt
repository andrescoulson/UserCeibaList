package com.andrescoulson.userceibalist.data.entity

import com.andrescoulson.userceibalist.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserEntity(
    @Json(name = "address")
    val address: Address,
    @Json(name = "company")
    val company: Company,
    @Json(name = "email")
    val email: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "phone")
    val phone: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "website")
    val website: String
)

fun UserEntity.toMap(): User = this.run {
    User(
        name = name,
        id = id,
        phone = phone,
        email = email
    )
}
fun UserEntity.toDbEntityMap(): com.andrescoulson.userceibalist.data.resources.local.db.entity.UserEntity = this.run {
    com.andrescoulson.userceibalist.data.resources.local.db.entity.UserEntity(
        name = name,
        id = id,
        phone = phone,
        email = email
    )
}


