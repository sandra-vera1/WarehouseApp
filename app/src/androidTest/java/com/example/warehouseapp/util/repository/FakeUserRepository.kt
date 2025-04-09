package com.example.warehouseapp.util.repository

import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUserRepository : UserRepository {
    private val users = MutableStateFlow<List<User>>(emptyList())

    fun addUser(user: User) {
        users.value += user
    }

    override fun getUserStream(username: String, password: String): Flow<User?> {
        return users.map { list ->
            list.find { it.userName == username && it.password == password }
        }
    }

    override suspend fun insertUser(user: User) {
        TODO("Not implemented because this is for testing only")
    }
}