package com.example.warehouseapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

open class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    open fun getUser(userName: String, password: String): Flow<User?> {
        return userRepository.getUserStream(userName, password)
    }


    companion object {
        fun Factory(repository: UserRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return UserViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}