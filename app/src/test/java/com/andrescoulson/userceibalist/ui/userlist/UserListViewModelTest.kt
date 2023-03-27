package com.andrescoulson.userceibalist.ui.userlist

import app.cash.turbine.test
import com.andrescoulson.userceibalist.domain.model.User
import com.andrescoulson.userceibalist.domain.usecase.GetPostListByIdUseCase
import com.andrescoulson.userceibalist.domain.usecase.GetUserListUseCase
import com.andrescoulson.userceibalist.test.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    @RelaxedMockK
    private lateinit var getPostListByIdUseCase: GetPostListByIdUseCase

    @RelaxedMockK
    private lateinit var getUserListUseCase: GetUserListUseCase

    @get:Rule
    var rule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test view model initialization`() {
        // Given
        coEvery { getUserListUseCase() } returns flowOf(emptyList())

        // When
        val viewModel = UserListViewModel(getUserListUseCase, getPostListByIdUseCase)

        // Then
        assertEquals(UiState.Loading, viewModel.userListUiState.value)
        assertEquals("", viewModel.searchText.value)
        assertNull(viewModel.userSelected.value)
        assertEquals(UiState.Loading, viewModel.postListUiState.value)
    }

    @Test
    fun `given successful user list retrieval, when viewmodel initialized, then user list is loaded correctly`() = runTest {
        // Given
        val userList = listOf(
            User(id = 1, name = "User 1", email = "user1@example.com", phone = "334535"),
            User(id = 2, name = "User 2", email = "user2@example.com", phone = "23424")
        )
        coEvery { getUserListUseCase() } returns flowOf(userList)

        // When
        val viewModel = UserListViewModel(getUserListUseCase, getPostListByIdUseCase)

        // Then
        viewModel.userListUiState.test {
            assertEquals(UiState.Success(userList), awaitItem())
            coVerify(exactly = 2) { getUserListUseCase() }
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test(expected = Exception::class)
    fun `given unsuccessful user list retrieval, when viewmodel initialized, then error state is set`() = runTest {
        // Given
        val error = Exception("Error retrieving user list")
        coEvery { getUserListUseCase() } throws error

        // When
        val viewModel = UserListViewModel(getUserListUseCase, getPostListByIdUseCase)

        // Then
        viewModel.userListUiState.test {
            val excepcion = awaitItem()
            assertTrue(excepcion is UiState.Error)
            assertEquals(error, (excepcion as UiState.Error).throwable)
            coVerify(exactly = 1) { getUserListUseCase() }
            cancelAndConsumeRemainingEvents()
        }
    }

}