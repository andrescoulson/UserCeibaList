package com.andrescoulson.userceibalist.data.resources.local

import com.andrescoulson.userceibalist.data.entity.PostEntity
import com.andrescoulson.userceibalist.data.entity.UserEntity
import com.andrescoulson.userceibalist.data.entity.toDbEntityMap
import com.andrescoulson.userceibalist.data.entity.toMapDbEntity
import com.andrescoulson.userceibalist.data.resources.local.db.dao.PostDao
import com.andrescoulson.userceibalist.data.resources.local.db.dao.UserDao
import com.andrescoulson.userceibalist.data.resources.local.db.entity.toPostMap
import com.andrescoulson.userceibalist.data.resources.local.db.entity.toUserMap
import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val userDao: UserDao, private val postDao: PostDao) {

    val users: Flow<List<User>> = userDao.getUserList().map { items ->
        items.map { it.toUserMap() }
    }

    suspend fun addUsers(users: List<UserEntity>) {
        userDao.addUser(users.map { it.toDbEntityMap() })
    }

    val posts: Flow<List<Post>> = postDao.getPostList().map { items ->
        items.map { it.toPostMap() }
    }

    suspend fun addPosts(posts: List<PostEntity>) {
        postDao.addPosts(posts.map { it.toMapDbEntity() })
    }

    fun getPostsById(userId: Int): Flow<List<Post>> {
        return postDao.getPostsById(userId).map { items -> items.map { it.toPostMap() } }
    }

}