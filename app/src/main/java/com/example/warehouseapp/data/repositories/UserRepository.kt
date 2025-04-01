package com.example.warehouseapp.data.repositories

import com.example.warehouseapp.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /**
     * Retrieve an user from the given data source that matches with the [username] and [password].
     */
    fun getUserStream(username: String, password: String): Flow<User?>

    /**
     * Insert user in the data source
     */
    suspend fun insertUser(user: User)
}