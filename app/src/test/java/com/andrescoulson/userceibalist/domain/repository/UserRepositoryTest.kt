package com.andrescoulson.userceibalist.domain.repository

import app.cash.turbine.test
import com.andrescoulson.userceibalist.data.entity.Address
import com.andrescoulson.userceibalist.data.entity.Company
import com.andrescoulson.userceibalist.data.entity.Geo
import com.andrescoulson.userceibalist.data.entity.PostEntity
import com.andrescoulson.userceibalist.data.entity.UserEntity
import com.andrescoulson.userceibalist.data.entity.toMap
import com.andrescoulson.userceibalist.data.entity.toPostMap
import com.andrescoulson.userceibalist.data.repository.UserRepositoryImpl
import com.andrescoulson.userceibalist.data.resources.local.LocalDataSource
import com.andrescoulson.userceibalist.data.resources.remote.RemoteDataSource
import com.andrescoulson.userceibalist.domain.model.Post
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.test.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    @RelaxedMockK
    private lateinit var localDataSource: LocalDataSource

    @RelaxedMockK
    private lateinit var remoteDataSource: RemoteDataSource

    lateinit var userRepository: UserRepository

    @get:Rule
    var rule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userRepository = UserRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `When userList database is empty then return data from remoteSource`() = runTest {
        val userList = listOf(
            UserEntity(
                id = 1,
                name = "Andres",
                email = "andres@email.com",
                phone = "34534",
                address = Address(city = "ctg", geo = Geo("123", "12312"), street = "street", suite = "suite", zipcode = "130011"),
                company = Company(name = "ceiba", bs = "asdsd", catchPhrase = "asd"),
                username = "Guille",
                website = "andres.bobadilla"
            )
        )

        //Give
        coEvery { localDataSource.users } returns flowOf(emptyList())
        coEvery { remoteDataSource.getUserList() } returns userList

        //When
        val userListResponse = userRepository.getUserList()

        //Then

        userListResponse.test {
            val users = awaitItem()
            coVerify(exactly = 1) { remoteDataSource.getUserList() }
            coVerify(exactly = 1) { localDataSource.users }
            assert(userList.map { it.toMap() } == users)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `When userList database is not empty the return data and do not call remote source`() = runTest {
        val userList = listOf<User>(mockk<User>())

        //Given
        coEvery { localDataSource.users } returns flowOf(userList)
        coEvery { remoteDataSource.getUserList() } returns emptyList()

        //When
        val userListResponse = userRepository.getUserList()

        //Then
        userListResponse.test {
            val users = awaitItem()
            coVerify(exactly = 0) { remoteDataSource.getUserList() }
            coVerify(exactly = 1) { localDataSource.users }
            assert(userList == users)
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `When getPostById database is not empty the return data and do not call remote source`() = runTest {
        val postList = listOf(mockk<Post>())

        //Given
        coEvery { localDataSource.getPostsById(any()) } returns flowOf(postList)
        coEvery { remoteDataSource.getPostsById(any()) } returns emptyList()

        //When
        val postListByIdResponse = userRepository.getPostsById(userId = 1)

        //Then
        postListByIdResponse.test {
            val posts = awaitItem()
            coVerify(exactly = 0) { remoteDataSource.getPostsById(1) }
            coVerify(exactly = 1) { localDataSource.getPostsById(1) }
            assert(postList == posts)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `When getPostById database is empty the return data from remote source`() = runTest {
        val postList = listOf(
            PostEntity(
                userId = 1,
                body = "Body",
                title = "title",
                id = 34
            )
        )

        //Given
        coEvery { localDataSource.getPostsById(any()) } returns flowOf(emptyList())
        coEvery { remoteDataSource.getPostsById(any()) } returns postList

        //When
        val postListByIdResponse = userRepository.getPostsById(userId = 1)

        //Then
        postListByIdResponse.test {
            val posts = awaitItem()
            coVerify(exactly = 1) { remoteDataSource.getPostsById(1) }
            coVerify(exactly = 1) { localDataSource.getPostsById(1) }
            assert(postList.map { it.toPostMap() } == posts)
            cancelAndConsumeRemainingEvents()
        }
    }


}