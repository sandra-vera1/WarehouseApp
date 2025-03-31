package com.example.warehouseapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FooterViewModel : ViewModel() {
    private val _navigateToAnotherScreen = MutableLiveData<Boolean>()
    val navigateToAnotherScreen: LiveData<Boolean> get() = _navigateToAnotherScreen

    fun onNavigate() {
        _navigateToAnotherScreen.value = true
    }

    fun doneNavigating() {
        _navigateToAnotherScreen.value = false
    }
}