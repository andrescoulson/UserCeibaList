package com.andrescoulson.userceibalist.domain.usecase

import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostList @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<List<Post>> {
        return userRepository.getPosts()
    }
}