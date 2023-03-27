package com.andrescoulson.userceibalist.data.resources.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.andrescoulson.userceibalist.data.resources.local.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * from PostEntity")
    fun getPostList(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPosts(items: List<PostEntity>)

    @Update
    suspend fun updateUser(item: PostEntity)

    @Query("SELECT * FROM PostEntity WHERE userId = :userId")
    fun getPostsById(userId: Int): Flow<List<PostEntity>>
}