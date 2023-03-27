package com.andrescoulson.userceibalist.domain.usecase

import app.cash.turbine.test
import com.andrescoulson.userceibalist.domain.model.Post
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetPostListByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    lateinit var getPostListByIdUseCase: GetPostListByIdUseCase

    @get:Rule
    var rule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getPostListByIdUseCase = GetPostListByIdUseCase((userRepository))
    }

    @Test
    fun `when use getPostListByIdUseCase verify Flow emits correct value`() = runTest {
        val postList = listOf(mockk<Post>())
        val userMock = User(phone = "phone", email = "email@emial.com", name = "Andres", id = 1)

        //Given
        coEvery { userRepository.getPostsById(userMock.id) } returns flowOf(postList)

        //When
        val postListFlow = getPostListByIdUseCase(userMock)

        //Then
        postListFlow.test {
            val postsResponse = awaitItem()
            assert(postList == postsResponse)
            cancelAndConsumeRemainingEvents()
        }
    }

}