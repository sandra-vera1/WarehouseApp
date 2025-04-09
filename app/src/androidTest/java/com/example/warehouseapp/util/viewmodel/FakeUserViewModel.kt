package com.example.warehouseapp.util.viewmodel

import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.data.repositories.UserRepository
import com.example.warehouseapp.viewmodels.UserViewModel
import kotlinx.coroutines.flow.Flow

class FakeUserViewModel(private val fakeRepository: UserRepository) : UserViewModel(fakeRepository) {
    override fun getUser(userName: String, password: String): Flow<User?> {
        return fakeRepository.getUserStream(userName, password)
    }
}