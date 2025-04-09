package com.example.warehouseapp.user

import com.example.warehouseapp.data.database.UserDao
import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.data.repositories.OfflineUserRepository
import com.example.warehouseapp.utils.UserRoles.ROLE_ADMIN
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfflineUserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var repository: OfflineUserRepository

    @Before
    fun setUp() {
        userDao = mockk(relaxed = true)
        repository = OfflineUserRepository(userDao)
    }

    @Test
    fun getUserStream_returns_single_user() = runTest {
        val user = User(
            id = 2,
            userName = "user",
            password = "12345",
            role = ROLE_ADMIN
        )
        every { userDao.getUser("user", "12345") } returns flowOf(user)

        val result = repository.getUserStream("user", "12345").firstOrNull()
        assertNotNull(result)
        assertEquals(user, result)
    }
}