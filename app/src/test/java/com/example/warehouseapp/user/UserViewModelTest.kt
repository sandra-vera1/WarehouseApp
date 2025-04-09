package com.example.warehouseapp.user

import app.cash.turbine.test
import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.data.repositories.UserRepository
import com.example.warehouseapp.utils.UserRoles.ROLE_ADMIN
import com.example.warehouseapp.viewmodels.UserViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        userRepository = mockk()
        userViewModel = UserViewModel(userRepository)
    }

    @Test
    fun getUser_should_return_user_from_repository() = runTest {
        val user = User(id = 1, userName = "usertest", password = "12345", role = ROLE_ADMIN)
        every { userRepository.getUserStream("usertest", "12345") } returns flowOf(user)
        userViewModel.getUser("usertest", "12345").test {
            assertEquals(user, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}