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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GetPostListTest {

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    lateinit var getPostList: GetPostList

    @get:Rule
    var rule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getPostList = GetPostList((userRepository))
    }

    @Test
    fun `when use getPosts returns correct value`() = runTest {
        val postList = listOf(mockk<Post>())

        //Given
        coEvery { userRepository.getPosts() } returns flowOf(postList)

        //When
        val postListFlow = getPostList()

        //Then
        postListFlow.test {
            val postsResponse = awaitItem()
            assert(postList == postsResponse)
            cancelAndConsumeRemainingEvents()
        }
    }

}