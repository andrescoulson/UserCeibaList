package com.andrescoulson.userceibalist.domain.usecase

import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getUserList()
    }
}