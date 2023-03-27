package com.andrescoulson.userceibalist.data.resources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrescoulson.userceibalist.data.resources.local.db.dao.PostDao
import com.andrescoulson.userceibalist.data.resources.local.db.dao.UserDao
import com.andrescoulson.userceibalist.data.resources.local.db.entity.PostEntity
import com.andrescoulson.userceibalist.data.resources.local.db.entity.UserEntity
import javax.inject.Inject

@Database(entities = [UserEntity::class, PostEntity::class], version = 1)
abstract class UserDatabase() : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}