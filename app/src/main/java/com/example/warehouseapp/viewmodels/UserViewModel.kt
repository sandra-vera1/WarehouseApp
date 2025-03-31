package com.example.warehouseapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.warehouseapp.data.User
import com.example.warehouseapp.data.userList

class UserViewModel : ViewModel(){
        private val _users = mutableStateListOf<User>()
        val users: SnapshotStateList<User> get() = _users

        init {
            _users.addAll(userList)
        }

        fun getUser(userName: String, password: String) : User?{
            val index = _users.indexOfFirst { it.userName == userName && it.password == password }
            return if(index == -1) null else _users[index]
        }
    }