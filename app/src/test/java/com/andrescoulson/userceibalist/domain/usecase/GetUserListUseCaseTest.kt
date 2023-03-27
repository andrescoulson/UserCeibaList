package com.andrescoulson.userceibalist.domain.usecase

import app.cash.turbine.test
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.domain.repository.UserRepository
import com.andrescoulson.userceibalist.test.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserListUseCaseTest {

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    lateinit var getUserListUseCase: GetUserListUseCase

    @get:Rule
    var rule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getUserListUseCase = GetUserListUseCase((userRepository))
    }


    @Test
    fun `when getUserListUseCase should be response a flow with user list `() = runTest {
        //Given
        val userList = listOf(mockk<User>(), mockk(), mockk())
        coEvery { userRepository.getUserList() } returns flowOf(userList)

        //When
        val usersFlow = getUserListUseCase()

        //Then
        usersFlow.test {
            val usersValue = awaitItem()

            assertEquals(userList, usersValue)

            cancelAndConsumeRemainingEvents()
        }
    }
}
