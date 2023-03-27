package com.andrescoulson.userceibalist.data.resources.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.andrescoulson.userceibalist.data.resources.local.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * from UserEntity")
    fun getUserList(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(users: List<UserEntity>)

    @Update
    suspend fun updateUser(item: UserEntity)

    @Query("SELECT COUNT(*) FROM UserEntity")
    fun getCount(): Int
}