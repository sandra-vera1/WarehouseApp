package com.example.warehouseapp.data.repositories

import com.example.warehouseapp.data.database.UserDao
import com.example.warehouseapp.data.models.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getUserStream(username: String, password: String): Flow<User?> = userDao.getUser(username, password)

    override suspend fun insertUser(user: User) = userDao.insert(user)
}